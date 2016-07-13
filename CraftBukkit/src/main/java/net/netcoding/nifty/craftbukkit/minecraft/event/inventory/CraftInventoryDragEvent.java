package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.event.inventory.InventoryDragEvent;

public class CraftInventoryDragEvent extends CraftInventoryInteractEvent implements InventoryDragEvent {

	public CraftInventoryDragEvent(org.bukkit.event.inventory.InventoryDragEvent inventoryDragEvent) {
		super(inventoryDragEvent);
	}

	@Override
	public org.bukkit.event.inventory.InventoryDragEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryDragEvent)super.getHandle();
	}

}