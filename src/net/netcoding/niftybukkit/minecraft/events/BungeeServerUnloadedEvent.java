package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BungeeServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeServerUnloadedEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient BungeeServer server;

	public BungeeServerUnloadedEvent(BungeeServer server) {
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