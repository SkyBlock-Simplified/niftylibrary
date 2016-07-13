package net.netcoding.nifty.common.mojang;

import net.netcoding.nifty.common.api.plugin.MinecraftHelper;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;

public class MinecraftMojangCache<T extends MinecraftMojangProfile> extends MinecraftHelper {

	private final T profile;

	public MinecraftMojangCache(MinecraftPlugin plugin, T profile) {
		super(plugin);
		this.profile = profile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		MinecraftMojangProfile profile = null;

		if (obj instanceof MinecraftMojangCache)
			profile = ((MinecraftMojangCache)obj).getProfile();
		else if (obj instanceof MinecraftMojangProfile)
			profile = (MinecraftMojangProfile)obj;

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
		return this.getProfile().isOnline();
	}

}