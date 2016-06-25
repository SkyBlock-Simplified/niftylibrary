package net.netcoding.nifty.common._new_.minecraft.event.bungee;

import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeServer;

public final class BungeeServerLoadedEvent implements BungeeEvent {

	private final transient BungeeServer server;

	public BungeeServerLoadedEvent(BungeeServer server) {
		this.server = server;
	}

	public BungeeServer getServer() {
		return this.server;
	}

}