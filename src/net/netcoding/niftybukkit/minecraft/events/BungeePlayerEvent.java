package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class BungeePlayerEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final BungeeServer server;
	private final String playerName;

	BungeePlayerEvent(BungeeServer server, String playerName) {
		this.server = server;
		this.playerName = playerName;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public BungeeServer getServer() {
		return this.server;
	}

}
