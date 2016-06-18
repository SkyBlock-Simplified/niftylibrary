package net.netcoding.nifty.common._new_.minecraft.event.bungee;

import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common._new_.minecraft.event.profile.ProfileEvent;

public abstract class BungeeProfileEvent extends ProfileEvent {

	protected BungeeProfileEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public BungeeServer<BukkitMojangProfile> getServer() {
		return this.getProfile().getServer();
	}

}
