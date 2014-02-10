package net.netcoding.niftybukkit.minecraft.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.netcoding.niftybukkit.minecraft.BukkitServer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BungeeLoadedEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient List<BukkitServer> serverList;

	public BungeeLoadedEvent(Collection<BukkitServer> serverList) {
		this.serverList = new ArrayList<>(serverList);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public List<BukkitServer> getServers() {
		return Collections.unmodifiableList(serverList);
	}

}