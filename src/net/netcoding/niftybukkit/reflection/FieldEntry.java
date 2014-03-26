package net.netcoding.niftybukkit.reflection;

public class FieldEntry {

	String key;
	Object value;

	public FieldEntry(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public Object getValue() {
		return this.value;
	}

}