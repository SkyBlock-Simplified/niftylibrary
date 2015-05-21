package net.netcoding.niftybukkit.mojang;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMojangCache extends BukkitHelper {

	private final BukkitMojangProfile profile;

	public BukkitMojangCache(JavaPlugin plugin, BukkitMojangProfile profile) {
		super(plugin);
		this.profile = profile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof BukkitMojangCache)) return false;
		BukkitMojangCache cache = (BukkitMojangCache)obj;
		return this.getProfile().equals(cache.getProfile());
	}

	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

	public OfflinePlayer getOfflinePlayer() {
		return this.getProfile().getOfflinePlayer();
	}

	public boolean hasPermissions(String... permissions) {
		return super.hasPermissions(this.getProfile(), permissions);
	}

	@Override
	public int hashCode() {
		return this.getProfile().hashCode();
	}

	public boolean isOnlineLocally() {
		return this.getProfile().isOnlineLocally();
	}

	public boolean isOnlineAnywhere() {
		return this.getProfile().isOnlineAnywhere();
	}

}