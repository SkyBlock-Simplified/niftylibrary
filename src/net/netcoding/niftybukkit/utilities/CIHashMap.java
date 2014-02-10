package net.netcoding.niftybukkit.utilities;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class CIHashMap<V> extends ConcurrentHashMap<String, V> {

	@Override
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}

	public V get(String key) {
		return super.get(key.toLowerCase());
	}

}