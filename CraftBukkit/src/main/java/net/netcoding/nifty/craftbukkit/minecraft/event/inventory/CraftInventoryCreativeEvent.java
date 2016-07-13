package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.event.inventory.InventoryCreativeEvent;

public class CraftInventoryCreativeEvent extends CraftInventoryClickEvent implements InventoryCreativeEvent {

	public CraftInventoryCreativeEvent(org.bukkit.event.inventory.InventoryCreativeEvent inventoryCreativeEvent) {
		super(inventoryCreativeEvent);
	}

	@Override
	public org.bukkit.event.inventory.InventoryCreativeEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryCreativeEvent)super.getHandle();
	}

}