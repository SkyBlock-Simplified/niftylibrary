package net.netcoding.niftybukkit.mojang;

import java.util.UUID;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MojangProfile {

	private String id;
	private UUID uuid;
	private String name;
	private int updated = (int)(System.currentTimeMillis() / 1000);
	private static final Pattern UUID_FIX = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

	private MojangProfile() { }

	public boolean belongsTo(OfflinePlayer oplayer) {
		if (oplayer == null) return false;
		if (!oplayer.getUniqueId().equals(this.getUniqueId())) return false;
		if (!oplayer.getName().equals(this.getName())) return false;
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		MojangProfile profile = (MojangProfile)obj;
		if (!this.getUniqueId().equals(profile.getUniqueId())) return false;
		if (!this.getName().equals(profile.getName())) return false;
		if (this.updated > profile.updated) return false; 
		return true;
	}

	// TODO: Send update across BungeeCord to update players name
	// if change is detected.
	public String getName() {
		Player player = NiftyBukkit.getPlugin().getServer().getPlayer(this.getUniqueId());

		if (player == null || player.getName().equals(this.name))
			return this.name;
		else
			return this.name = player.getName();
	}

	public OfflinePlayer getOfflinePlayer() {
		return NiftyBukkit.getPlugin().getServer().getOfflinePlayer(this.getUniqueId());
	}

	public UUID getUniqueId() {
		if (this.uuid == null)
			this.uuid = UUID.fromString(UUID_FIX.matcher(this.id.replace("-", "")).replaceAll("$1-$2-$3-$4-$5"));

		return this.uuid;
	}

	public boolean hasExpired() {
		return System.currentTimeMillis() / 1000 - this.updated >= 600;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (StringUtil.isEmpty(this.getName()) ? 0 : this.getName().hashCode());
		result = prime * result + (StringUtil.isEmpty(this.id) ? 0 : this.getUniqueId().hashCode());
		result = prime * result + this.updated;
		return result;
	}

	@Override
	public String toString() {
		return StringUtil.format("'{'{0},{1}'}'", this.getUniqueId(), this.getName());
	}

}