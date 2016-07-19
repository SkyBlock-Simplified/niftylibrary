package net.netcoding.nifty.common.mojang;

import net.netcoding.nifty.common.api.plugin.MinecraftHelper;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;

public abstract class MinecraftMojangCache<T extends MinecraftMojangProfile> extends MinecraftHelper {

	private final T profile;

	protected MinecraftMojangCache(MinecraftPlugin plugin, T profile) {
		super(plugin);
		this.profile = profile;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == this)
			return true;
		else {
			MinecraftMojangProfile profile = null;

			if (obj instanceof MinecraftMojangCache)
				profile = ((MinecraftMojangCache)obj).getProfile();
			else if (obj instanceof MinecraftMojangProfile)
				profile = (MinecraftMojangProfile)obj;

			return this.getProfile().equals(profile);
		}
	}

	public final T getProfile() {
		return this.profile;
	}

	public final OfflinePlayer getOfflinePlayer() {
		return this.getProfile().getOfflinePlayer();
	}

	public final boolean hasPermissions(String... permissions) {
		return super.hasPermissions(this.getProfile(), permissions);
	}

	@Override
	public final int hashCode() {
		return this.getProfile().hashCode();
	}

	public final boolean isOnlineLocally() {
		return this.getProfile().isOnlineLocally();
	}

	public final boolean isOnline() {
		return this.getProfile().isOnline();
	}

}