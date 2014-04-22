package net.netcoding.niftybukkit.mojang;

import java.util.UUID;
import java.util.regex.Pattern;

public class MojangProfile {

	private String id;
	private UUID uuid;
	private String name;
	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

	public MojangProfile(String name, String id) {
		this.name = name;
		this.id = id.replace("-", "");
	}

	public UUID getUniqueId() {
		if (this.uuid == null)
			this.uuid = UUID.fromString(UUID_FIX.matcher(this.id.replace("-", "")).replaceAll("$1-$2-$3-$4-$5"));

		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

}