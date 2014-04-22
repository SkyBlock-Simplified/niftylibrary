package net.netcoding.niftybukkit.database;

public enum TriggerEvent {

	INSERT("Insert"),
	DELETE("Delete"),
	UPDATE("Update");

	private String id;

	private TriggerEvent(String paramString) {
		this.id = paramString;
	}

	public String toLowercase() {
		return this.id.toLowerCase();
	}

	public String toString() {
		return this.id;
	}

	public String toUppercase() {
		return this.id.toUpperCase();
	}

}