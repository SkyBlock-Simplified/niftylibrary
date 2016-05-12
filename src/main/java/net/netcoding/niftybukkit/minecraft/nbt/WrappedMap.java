package net.netcoding.niftybukkit.minecraft.nbt;

import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a map that wraps another map and automatically
 * converts entries of its type and another exposed type.
 */
@SuppressWarnings("unchecked")
abstract class WrappedMap extends AbstractMap<String, Object> implements Wrapper {

	protected final ConcurrentMap<String, Class<?>> supported = new ConcurrentMap<>();
	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final Map<String, Object> original;
	private final Object handle;

	WrappedMap(Object handle, Map<String, Object> original) {
		this.handle = handle;
		this.original = original;
	}

	private Object adjustIncoming(String key, Object value) {
		if (value == null)
			return null;

		if (WrappedList.class.isAssignableFrom(value.getClass()) || WrappedMap.class.isAssignableFrom(value.getClass()))
			return value;

		Class<?> clazz = value.getClass();

		if (!clazz.isPrimitive()) {
			if (clazz.isArray()) {
				if (!int[].class.equals(clazz) && !byte[].class.equals(clazz)) {
					this.supported.put(key, clazz);
					value = NbtFactory.createList((Object[])value);
				}
			} else {
				this.supported.put(key, clazz);

				if (CharSequence.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (UUID.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (BigDecimal.class.isAssignableFrom(clazz))
					value = ((BigDecimal)value).doubleValue();
				else if (BigInteger.class.isAssignableFrom(clazz))
					value = ((BigInteger)value).longValue();
				else if (Collection.class.isAssignableFrom(clazz))
					value = NbtFactory.createList((Collection<?>)value);
				else if (Map.class.isAssignableFrom(clazz)) {
					NbtCompound compound = NbtFactory.createCompound();
					compound.putAll((Map<String, Object>)value);
					value = compound;
				} else
					this.supported.remove(key);
			}
		} else if (value instanceof Boolean) {
			this.supported.put(key, boolean.class);
			value = (byte) ((boolean) value ? 1 : 0);
		}

		return value;
	}

	private Object adjustOutgoing(Object key, Object value) {
		if (value == null)
			return null;

		if (this.supported.containsKey(key)) {
			Class<?> clazz = this.supported.get(key);

			if (boolean.class.equals(clazz))
				value = (byte)value > 0;
			else {
				if (UUID.class.equals(clazz))
					value = UUID.fromString(value.toString());
				else if (BigDecimal.class.equals(clazz))
					value = BigDecimal.valueOf((double)value);
				else if (BigInteger.class.equals(clazz))
					value = BigInteger.valueOf((long)value);
				else if (Map.class.isAssignableFrom(clazz)) {
					NbtCompound compound = (NbtCompound)value;
					boolean adjusted = false;

					if (!Map.class.equals(clazz)) {
						Reflection refCollection = new Reflection(clazz);
						Map<String, Object> map = (Map<String, Object>)refCollection.newInstance();
						refCollection.invokeMethod("putAll", map, compound);
						adjusted = true;
					}

					if (!adjusted)
						value = compound;
				} else if (Collection.class.isAssignableFrom(clazz) || clazz.isArray()) {
					NbtList nbtList = (NbtList)value;

					if (!clazz.isArray()) {
						boolean adjusted = false;

						if (!Collection.class.equals(clazz)) {
							Reflection refCollection = new Reflection(clazz);
							Object collection = refCollection.newInstance();
							refCollection.invokeMethod("addAll", collection, nbtList);
							adjusted = true;
						}

						if (!adjusted)
							value = nbtList;
					} else
						value = ListUtil.toArray(nbtList, clazz.getComponentType());
				}
			}
		}

		return value;
	}

	@Override
	public boolean containsKey(Object key) {
		return this.original.containsKey(key);
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
				return WrappedMap.this.original.size();
			}

			@Override
			public Iterator<Entry<String, Object>> iterator() {
				return WrappedMap.this.iterator();
			}

			@Override
			public boolean remove(Object obj) {
				return WrappedMap.this.remove(obj) != null;
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

	private Iterator<Entry<String, Object>> iterator() {
		final Iterator<Entry<String, Object>> proxy = this.original.entrySet().iterator();

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
					WrappedMap.this.supported.remove(this.current.getKey());
					this.current = null;
				}

				proxy.remove();
			}

		};
	}

	@Override
	public Object put(String key, Object value) {
		return this.wrapOutgoing(key, this.original.put(key, this.unwrapIncoming(key, value)));
	}

	@Override
	public void putAll(Map<? extends String, ?> map) {
		for (Map.Entry<? extends String, ?> entry : map.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public Object remove(Object key) {
		return this.wrapOutgoing(key, this.original.remove(key));
	}

	@Override
	public String toString() {
		List<String> output = new ArrayList<>();

		for (Map.Entry<String, Object> entry : this.entrySet()) {
			Object value = entry.getValue();
			value = (value.getClass().isArray() ? Arrays.deepToString((Object[])value) : value);
			output.add(StringUtil.format("{0}:{1}", entry.getKey(), value));
		}

		return StringUtil.format("'{'{0}'}'", (output.isEmpty() ? "" : StringUtil.implode(", ", output)));
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
		return NbtFactory.unwrapValue(key, this.adjustIncoming(key, wrapped));
	}

}