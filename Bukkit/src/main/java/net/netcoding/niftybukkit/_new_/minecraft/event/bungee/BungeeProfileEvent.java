package net.netcoding.niftybukkit._new_.minecraft.event.bungee;

import net.netcoding.niftybukkit._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.niftybukkit._new_.minecraft.event.profile.ProfileEvent;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

public abstract class BungeeProfileEvent extends ProfileEvent {

	protected BungeeProfileEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public BungeeServer<BukkitMojangProfile> getServer() {
		return this.getProfile().getServer();
	}

}
