package net.netcoding.niftybukkit.minecraft.nbt;

import com.google.common.collect.MapMaker;

import java.util.concurrent.ConcurrentMap;

/**
 * Represents a class for caching wrappers.
 */
final class CachedNativeWrapper {

	// Don't recreate wrapper objects
	private final ConcurrentMap<Object, Object> cache = new MapMaker().weakKeys().makeMap();

	public Object wrap(Object value) {
		Object current = cache.get(value);

		if (current == null) {
			current = NbtFactory.wrapNative(value);

			// Only cache composite objects
			if (current instanceof ConvertedMap || current instanceof ConvertedList)
				cache.put(value, current);
		}

		return current;
	}

}