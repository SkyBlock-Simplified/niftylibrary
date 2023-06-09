package net.netcoding.nifty.common.api.nbt;

import com.google.common.primitives.Primitives;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a map that wraps another map and automatically
 * converts entries of its type and another exposed type.
 */
abstract class WrappedMap extends AbstractMap<String, Object> implements Wrapper {

	private static final String SUPPORT = "support";
	private static final List<String> DO_NOT_SHOW = Arrays.asList("id", "Count", "Damage", "display", "ench", "HideFlags", SUPPORT);
	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final Map<String, Object> original;
	private final Object handle;
	private final boolean root;

	WrappedMap(Object handle, boolean root, Map<String, Object> original) {
		this.handle = handle;
		this.root = root;
		this.original = original;
	}

	private void addSupportKey(String key, Class<?> clazz) {
		if (!this.original.containsKey(SUPPORT))
			this.original.put(SUPPORT, Nifty.getNbtFactory().createCompound().getHandle());

		NbtCompound support = Nifty.getNbtFactory().fromCompound(this.original.get(SUPPORT));

		if (!support.containsKey(key))
			support.put(key, Nifty.getNbtFactory().createCompound());

		clazz = Primitives.wrap(clazz);
		NbtCompound keyCompound = support.get(key);
		keyCompound.put("package", clazz.getPackage().getName());
		keyCompound.put("class", clazz.getSimpleName());
	}

	private Object adjustIncoming(String key, Object value) {
		Object adjusted = NbtFactory.adjustIncoming(value);

		if (!Objects.equals(adjusted, value))
			this.addSupportKey(key, Primitives.unwrap(value.getClass()));

		return adjusted;
	}

	private Object adjustOutgoing(Object key, Object value) {
		if (value == null)
			return null;

		if (this.doNotShow(key))
			return null;

		if (!this.original.containsKey(SUPPORT))
			return value;

		NbtCompound support = Nifty.getNbtFactory().fromCompound(this.original.get(SUPPORT));

		if (support.containsKey(key)) {
			NbtCompound keyCompound = (NbtCompound)support.get(key);
			Class clazz = Primitives.unwrap(new Reflection(keyCompound.get("class"), keyCompound.get("package")).getClazz());
			value = NbtFactory.adjustOutgoing(value, clazz);
		}

		return value;
	}

	@Override
	public void clear() {
		this.original.clear();
		this.save();
	}

	@Override
	public boolean containsKey(Object key) {
		return !this.doNotShow(key) && this.original.containsKey(key);
	}

	private boolean doNotShow(Object key) {
		return this.isRoot() && DO_NOT_SHOW.contains(key);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return new AbstractSet<Entry<String, Object>>() {

			@Override
			public boolean add(Entry<String, Object> entry) {
				WrappedMap.this.put(entry.getKey(), entry.getValue());
				return true;
			}

			@Override
			public int size() {
				return WrappedMap.this.size();
			}

			@Override
			public Iterator<Entry<String, Object>> iterator() {
				return WrappedMap.this.iterator();
			}

			@Override
			public boolean remove(Object obj) {
				return WrappedMap.this.remove(obj) != null;
			}

			@Override
			public boolean removeAll(Collection<?> coll) {
				int size = this.size();
				coll.forEach(WrappedMap.this::remove);
				return this.size() < size;
			}

		};
	}

	@Override
	public Object get(Object key) {
		return this.wrapOutgoing(key, this.original.get(key));
	}

	@Override
	public final Object getHandle() {
		return this.handle;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	public final boolean isRoot() {
		return this.root;
	}

	private Iterator<Entry<String, Object>> iterator() {
		ConcurrentSet<Entry<String, Object>> entrySet = Concurrent.newSet(this.original.entrySet());
		entrySet.stream().filter(entry -> this.doNotShow(entry.getKey())).forEach(entrySet::remove);
		final Iterator<Entry<String, Object>> proxy = entrySet.iterator();

		return new Iterator<Entry<String, Object>>() {

			private Entry<String, Object> current = null;

			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public Entry<String, Object> next() {
				Entry<String, Object> entry = proxy.next();
				String key = entry.getKey();
				return this.current = new SimpleEntry<>(key, WrappedMap.this.wrapOutgoing(key, entry.getValue()));
			}

			@Override
			public void remove() {
				if (this.current != null) {
					WrappedMap.this.remove(this.current.getKey());
					this.current = null;
					WrappedMap.this.save();
				}
			}

		};
	}

	@Override
	public Set<String> keySet() {
		return this.original.keySet().stream().filter(key -> !this.doNotShow(key)).collect(Collectors.toSet());
	}

	public boolean notEmpty() {
		return !this.isEmpty();
	}

	@Override
	public Object put(String key, Object value) {
		if (this.doNotShow(key))
			return null;

		Object oldValue = this.wrapOutgoing(key, this.original.put(key, this.unwrapIncoming(key, value)));
		this.save();
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ?> map) {
		for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
			if (this.doNotShow(entry.getKey())) continue;
			this.original.put(entry.getKey(), this.unwrapIncoming(entry.getKey(), entry.getValue()));
		}

		this.save();
	}

	public void putJson(JsonObject json) {
		this.putAll(new Gson().<Map<String, Object>>fromJson(json.toString(), new TypeToken<Map<String, Object>>(){}.getType()));
	}

	@Override
	public Object remove(Object key) {
		if (this.doNotShow(key))
			return null;

		Object oldValue = this.wrapOutgoing(key, this.original.remove(key));
		this.removeSupportKey(key);
		this.save();
		return oldValue;
	}

	private void removeSupportKey(Object key) {
		Nifty.getNbtFactory().fromCompound(this.original.get(SUPPORT)).remove(key);
	}

	protected void save() { }

	@Override
	public int size() {
		return this.keySet().size();
	}

	public final String serialize() {
		List<String> output = new ArrayList<>();

		for (Map.Entry<String, Object> entry : this.entrySet()) {
			Object value = entry.getValue();
			value = (value.getClass().isArray() ? Arrays.deepToString((Object[])value) : value);

			if (WrappedMap.class.isAssignableFrom(value.getClass()))
				value = ((WrappedMap)value).serialize();

			if (WrappedList.class.isAssignableFrom(value.getClass()))
				value = ((WrappedList)value).serialize();

			output.add(StringUtil.format("{0}:{1}", entry.getKey(), value));
		}

		return StringUtil.format("'{'{0}'}'", (output.isEmpty() ? "" : StringUtil.implode(", ", output)));
	}

	@Override
	public String toString() {
		return this.serialize();
	}

	/**
	 * For converting NBT value back to Java value.
	 *
	 * @param key the nbt key.
	 * @param value the nbt value.
	 * @return java value of wrapped {@code value}.
	 */
	protected final Object wrapOutgoing(Object key, Object value) {
		return this.adjustOutgoing(key, this.cache.wrap(value));
	}

	/**
	 * Converts Java value to NBT value.
	 *
	 * @param key the nbt key.
	 * @param wrapped the java value.
	 * @return nbt type of passed {@code wrapped} value.
	 */
	protected final Object unwrapIncoming(String key, Object wrapped) {
		return NbtFactory.unwrapValue(this.adjustIncoming(key, wrapped));
	}

	@Override
	public Collection<Object> values() {
		return new AbstractCollection<Object>() {

			@Override
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {

					private final Iterator<Entry<String, Object>> i = WrappedMap.this.entrySet().iterator();

					@Override
					public boolean hasNext() {
						return i.hasNext();
					}

					@Override
					public Object next() {
						return i.next().getValue();
					}

					@Override
					public void remove() {
						i.remove();
					}

				};
			}

			@Override
			public int size() {
				return WrappedMap.this.size();
			}

			@Override
			public boolean isEmpty() {
				return WrappedMap.this.isEmpty();
			}

			@Override
			public void clear() {
				WrappedMap.this.clear();
			}

			@Override
			public boolean contains(Object value) {
				return WrappedMap.this.containsValue(value);
			}

		};
	}

}