package net.netcoding.niftybukkit.mojang;

import java.util.UUID;
import java.util.regex.Pattern;

public class MojangProfile {

	private String id;
	private UUID uuid;
	private String name;
	private long cached;
	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

	public MojangProfile(String name, String id) {
		this.name = name;
		this.id = id.replace("-", "");
		this.uuid = UUID.fromString(UUID_FIX.matcher(this.id).replaceAll("$1-$2-$3-$4-$5"));
		this.updateCacheTime();
	}

	public UUID getUniqueId() {
		if (this.hasExpired()) {
			//this.id = NiftyBukkit.getMojangRepository().searchByExactUsername(this.getName()).getUniqueId();
			this.updateCacheTime();
		}

		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	boolean hasExpired() {
		return this.cached == -1 ? false /* TODO: Bungee Support */ : System.currentTimeMillis() > this.cached;
	}

	void updateCacheTime() {
		this.cached = System.currentTimeMillis() + 60000;
	}

}