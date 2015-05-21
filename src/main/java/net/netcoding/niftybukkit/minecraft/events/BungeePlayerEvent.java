package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

class BungeePlayerEvent extends PlayerEvent {

	BungeePlayerEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public BungeeServer getServer() {
		return (BungeeServer)this.getProfile().getServer();
	}

}
