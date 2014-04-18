package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.mojang.MojangProfile;

public class BungeePlayerJoinEvent extends BungeePlayerEvent {

	public BungeePlayerJoinEvent(BungeeServer server, MojangProfile profile) {
		super(server, profile);
	}

}