package net.netcoding.niftybukkit.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;

public class InventoryCloseEvent extends InventoryEvent {

	public InventoryCloseEvent(Player player, Inventory inventory, Action action) {
		super(player, inventory, action);
	}

}