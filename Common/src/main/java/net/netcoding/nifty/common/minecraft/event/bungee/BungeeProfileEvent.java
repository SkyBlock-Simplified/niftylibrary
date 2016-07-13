package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public abstract class BungeeProfileEvent implements BungeeEvent, PlayerEvent {

	private final MinecraftMojangProfile profile;

	protected BungeeProfileEvent(MinecraftMojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public final MinecraftMojangProfile getProfile() {
		return this.profile;
	}

	public final BungeeServer<MinecraftMojangProfile> getServer() {
		return this.getProfile().getServer();
	}

}
