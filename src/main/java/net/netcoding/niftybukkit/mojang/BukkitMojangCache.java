package net.netcoding.niftybukkit.mojang;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMojangCache<T extends BukkitMojangProfile> extends BukkitHelper {

	private final T profile;

	public BukkitMojangCache(JavaPlugin plugin, T profile) {
		super(plugin);
		this.profile = profile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		BukkitMojangProfile profile = null;

		if (obj instanceof BukkitMojangCache)
			profile = ((BukkitMojangCache)obj).getProfile();
		else if (obj instanceof BukkitMojangProfile)
			profile = (BukkitMojangProfile)obj;

		return this.getProfile().equals(profile);
	}

	public T getProfile() {
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