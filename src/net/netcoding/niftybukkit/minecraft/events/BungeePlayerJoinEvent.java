package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;

public class BungeePlayerJoinEvent extends BungeePlayerEvent {

	public BungeePlayerJoinEvent(BungeeServer server, String playerName) {
		super(server, playerName);
	}

}