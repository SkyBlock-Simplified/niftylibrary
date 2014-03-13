package net.netcoding.niftybukkit.mojang;

public class MojangProfile {

	private String id;
	private String name;

	MojangProfile(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getUniqueId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

}