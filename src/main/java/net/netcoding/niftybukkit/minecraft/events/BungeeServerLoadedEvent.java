package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeServerLoadedEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient BungeeServer server;

	public BungeeServerLoadedEvent(BungeeServer server) {
		this.server = server;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BungeeServer getServer() {
		return this.server;
	}

}