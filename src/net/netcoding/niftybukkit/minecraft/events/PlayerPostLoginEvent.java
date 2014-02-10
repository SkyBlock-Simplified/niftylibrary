package net.netcoding.niftybukkit.minecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPostLoginEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient Player player;

	public PlayerPostLoginEvent(Player player) {
		this.player = player;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer() {
		return this.player;
	}

}