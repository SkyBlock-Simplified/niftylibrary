package net.netcoding.niftybukkit.inventory.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;

class InventoryEvent extends Event implements Cancellable {

	private static final transient HandlerList handlers = new HandlerList();
	private final transient Player player;
	private final transient Inventory inventory;
	private final transient Action action;
	private transient boolean cancelled = false;

	public InventoryEvent(Player player, Inventory inventory, Action action) {
		this.player = player;
		this.inventory = inventory;
		this.action = action;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Action getAction() {
		return this.action;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}