package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public abstract class BungeeProfileEvent implements BungeeEvent, PlayerEvent {

	private final BukkitMojangProfile profile;

	protected BungeeProfileEvent(BukkitMojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public final BukkitMojangProfile getProfile() {
		return this.profile;
	}

	public final BungeeServer<BukkitMojangProfile> getServer() {
		return this.getProfile().getServer();
	}

}
