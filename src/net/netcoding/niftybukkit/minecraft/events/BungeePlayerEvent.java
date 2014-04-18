package net.netcoding.niftybukkit.minecraft.events;

import java.util.UUID;

import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.mojang.MojangProfile;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class BungeePlayerEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final BungeeServer server;
	private final MojangProfile profile;

	BungeePlayerEvent(BungeeServer server, MojangProfile profile) {
		this.server = server;
		this.profile = profile;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getName() {
		return this.profile.getName();
	}

	public BungeeServer getServer() {
		return this.server;
	}

	public UUID getUniqueId() {
		return this.profile.getUniqueId();
	}

}
