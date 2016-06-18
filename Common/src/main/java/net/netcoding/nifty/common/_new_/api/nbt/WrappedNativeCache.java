package net.netcoding.nifty.common._new_.api.nbt;

import com.google.common.collect.MapMaker;

import java.util.concurrent.ConcurrentMap;

/**
 * Represents a class for caching wrappers.
 */
final class WrappedNativeCache {

	// Don't recreate wrapper objects
	private final ConcurrentMap<Object, Object> cache = new MapMaker().weakKeys().makeMap();

	public Object wrap(Object value) {
		Object current = this.cache.get(value);

		if (current == null) {
			current = NbtFactory.wrapNative(value);

			// Only cache composite objects
			if (current instanceof WrappedMap || current instanceof WrappedList)
				this.cache.put(value, current);
		}

		return current;
	}

}