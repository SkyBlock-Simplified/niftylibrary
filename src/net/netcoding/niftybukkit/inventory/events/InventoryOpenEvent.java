package net.netcoding.niftybukkit.inventory.events;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.Inventory;

public class InventoryOpenEvent extends InventoryEvent {

	public InventoryOpenEvent(Player player, Inventory inventory, Action action) {
		super(player, inventory, action);
	}

}