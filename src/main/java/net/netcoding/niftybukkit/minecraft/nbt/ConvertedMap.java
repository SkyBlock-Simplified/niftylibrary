package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Represents a map that wraps another map and automatically
 * converts entries of its type and another exposed type.
 */
class ConvertedMap extends AbstractMap<String, Object> implements Wrapper {

	private final Object handle;
	private final Map<String, Object> original;
	private final CachedNativeWrapper cache = new CachedNativeWrapper();

	public ConvertedMap(Object handle, Map<String, Object> original) {
		this.handle = handle;
		this.original = original;
	}

	@Override
	public boolean containsKey(Object key) {
		return this.original.containsKey(key);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return new AbstractSet<Entry<String, Object>>() {
			@Override
			public boolean add(Entry<String, Object> e) {
				String key = e.getKey();
				Object value = e.getValue();
				original.put(key, unwrapIncoming(key, value));
				return true;
			}

			@Override
			public int size() {
				return original.size();
			}

			@Override
			public Iterator<Entry<String, Object>> iterator() {
				return ConvertedMap.this.iterator();
			}
		};
	}

	// Performance
	@Override
	public Object get(Object key) {
		return this.wrapOutgoing(this.original.get(key));
	}

	@Override
	public Object getHandle() {
		return this.handle;
	}

	private Iterator<Entry<String, Object>> iterator() {
		final Iterator<Entry<String, Object>> proxy = original.entrySet().iterator();

		return new Iterator<Entry<String, Object>>() {
			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public Entry<String, Object> next() {
				Entry<String, Object> entry = proxy.next();
				return new SimpleEntry<>(entry.getKey(), wrapOutgoing(entry.getValue()));
			}

			@Override
			public void remove() {
				proxy.remove();
			}
		};
	}

	// Modification
	@Override
	public Object put(String key, Object value) {
		return this.wrapOutgoing(this.original.put(key, this.unwrapIncoming(key, value)));
	}

	@Override
	public Object remove(Object key) {
		return this.wrapOutgoing(this.original.remove(key));
	}

	// For converting back and forth
	protected Object wrapOutgoing(Object value) {
		return this.cache.wrap(value);
	}

	protected Object unwrapIncoming(String key, Object wrapped) {
		return NbtFactory.unwrapValue(key, wrapped);
	}


}