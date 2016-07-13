package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.event.inventory.InventoryEvent;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.craftbukkit.minecraft.event.CraftEvent;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventoryView;

abstract class CraftInventoryEvent extends CraftEvent implements InventoryEvent {

	private final InventoryView view;

	public CraftInventoryEvent(org.bukkit.event.inventory.InventoryEvent inventoryEvent) {
		super(inventoryEvent);
		this.view = new CraftInventoryView(inventoryEvent.getView());
	}

	@Override
	public org.bukkit.event.inventory.InventoryEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryEvent)super.getHandle();
	}

	@Override
	public InventoryView getView() {
		return this.view;
	}

}