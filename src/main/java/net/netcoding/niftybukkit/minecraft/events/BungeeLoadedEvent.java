package net.netcoding.niftybukkit.minecraft.events;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeLoadedEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient Set<BungeeServer> serverList;

	public BungeeLoadedEvent(Collection<BungeeServer> serverList) {
		this.serverList = new HashSet<>(serverList);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Set<BungeeServer> getServers() {
		return Collections.unmodifiableSet(serverList);
	}

}