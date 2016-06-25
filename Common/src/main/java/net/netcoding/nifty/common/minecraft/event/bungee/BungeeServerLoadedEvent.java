package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;

public final class BungeeServerLoadedEvent implements BungeeEvent {

	private final transient BungeeServer server;

	public BungeeServerLoadedEvent(BungeeServer server) {
		this.server = server;
	}

	public BungeeServer getServer() {
		return this.server;
	}

}