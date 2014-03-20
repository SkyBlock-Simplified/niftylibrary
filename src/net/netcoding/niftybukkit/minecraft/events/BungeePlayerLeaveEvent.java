package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;

public class BungeePlayerLeaveEvent extends BungeePlayerEvent {

	public BungeePlayerLeaveEvent(BungeeServer server, String playerName) {
		super(server, playerName);
	}

}