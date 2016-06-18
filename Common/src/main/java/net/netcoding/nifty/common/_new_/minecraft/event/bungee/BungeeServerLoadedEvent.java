package net.netcoding.nifty.common._new_.minecraft.event.bungee;

import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common._new_.minecraft.event.Event;

public class BungeeServerLoadedEvent extends Event {

	private final transient BungeeServer server;

	public BungeeServerLoadedEvent(BungeeServer server) {
		this.server = server;
	}

	public BungeeServer getServer() {
		return this.server;
	}

}