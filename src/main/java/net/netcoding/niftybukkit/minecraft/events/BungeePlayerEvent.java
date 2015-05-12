package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftycore.mojang.MojangProfile;

class BungeePlayerEvent extends PlayerEvent {

	BungeePlayerEvent(MojangProfile profile) {
		super(profile);
	}

	public BungeeServer getServer() {
		return (BungeeServer)this.getProfile().getServer();
	}

}
