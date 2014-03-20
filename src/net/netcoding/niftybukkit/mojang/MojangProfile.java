package net.netcoding.niftybukkit.mojang;

import net.netcoding.niftybukkit.NiftyBukkit;

public class MojangProfile {

	private String id;
	private String name;
	private long cached;

	MojangProfile(String name, String id) {
		this.name = name;
		this.id = id;
		this.updateCacheTime();
	}

	public String getUniqueId() {
		if (this.hasExpired()) {
			this.id = NiftyBukkit.getMojangRepository().searchByExactUsername(this.getName()).getUniqueId();
			this.updateCacheTime();
		}

		return this.id;
	}

	public String getName() {
		return this.name;
	}

	boolean hasExpired() {
		return this.cached == -1 ? false /* TODO: Bungee Support */ : System.currentTimeMillis() > this.cached;
	}

	void updateCacheTime() {
		this.cached = System.currentTimeMillis() + 120;
	}

}