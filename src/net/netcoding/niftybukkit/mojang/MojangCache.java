package net.netcoding.niftybukkit.mojang;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class MojangCache extends BukkitHelper {

	private final MojangProfile profile;

	public MojangCache(JavaPlugin plugin, MojangProfile profile) {
		super(plugin);
		this.profile = profile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof MojangCache)) return false;
		MojangCache cache = (MojangCache)obj;
		return this.getProfile().equals(cache.getProfile());
	}

	public MojangProfile getProfile() {
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

	public boolean isOnline() {
		return this.getOfflinePlayer().isOnline();
	}

	public boolean isOnlineAnywhere() {
		return this.getProfile().isOnline();
	}

}