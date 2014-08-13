package net.netcoding.niftybukkit.mojang;

import java.util.UUID;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.util.StringUtil;

public class MojangProfile {

	private String id;
	private UUID uuid;
	private String name;
	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

	public MojangProfile(String name, String id) {
		this.name = name;
		this.id = id.replace("-", "");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		MojangProfile profile = (MojangProfile)obj;
		if (!this.getUniqueId().equals(profile.getUniqueId())) return false;
		if (!this.getName().equals(profile.getName())) return false;
		return true;
	}

	public UUID getUniqueId() {
		if (this.uuid == null)
			this.uuid = UUID.fromString(UUID_FIX.matcher(this.id.replace("-", "")).replaceAll("$1-$2-$3-$4-$5"));

		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (StringUtil.isEmpty(this.getName()) ? 0 : this.getName().hashCode());
		result = prime * result + (StringUtil.isEmpty(this.id) ? 0 : this.getUniqueId().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return StringUtil.format("{{0},{1}}", this.getUniqueId(), this.getName());
	}

}