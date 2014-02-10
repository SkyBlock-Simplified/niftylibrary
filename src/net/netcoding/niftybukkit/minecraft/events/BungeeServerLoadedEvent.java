package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.minecraft.BukkitServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeServerLoadedEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient BukkitServer server;

	public BungeeServerLoadedEvent(BukkitServer server) {
		this.server = server;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BukkitServer getServer() {
		return this.server;
	}

}