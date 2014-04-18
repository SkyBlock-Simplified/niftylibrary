package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.mojang.MojangProfile;

public class BungeePlayerLeaveEvent extends BungeePlayerEvent {

	public BungeePlayerLeaveEvent(BungeeServer server, MojangProfile profile) {
		super(server, profile);
	}

}