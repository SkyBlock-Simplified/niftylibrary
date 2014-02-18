package net.netcoding.niftybukkit.yaml;

import java.util.HashMap;
import java.util.Map;

public class CacheConfig {

	private HashMap<String, Object> lookupTable = new HashMap<>();

	public void set(String fullPath, Object section) {
		lookupTable.put(fullPath, section);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String fullpath) {
		return (T) lookupTable.get(fullpath);
	}

	public boolean has(String fullpath) {
		return lookupTable.containsKey(fullpath);
	}

	public void niceOutput() {
		for(Map.Entry<String, Object> lookupEntry : lookupTable.entrySet())
			System.out.println(lookupEntry.getKey() + "->" + lookupEntry.getValue().getClass().getName());
	}

}