package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryCloseEvent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftInventoryCloseEvent extends CraftInventoryEvent implements InventoryCloseEvent {

	public CraftInventoryCloseEvent(org.bukkit.event.inventory.InventoryCloseEvent inventoryCloseEvent) {
		super(inventoryCloseEvent);
	}

	@Override
	public org.bukkit.event.inventory.InventoryCloseEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryCloseEvent)super.getHandle();
	}

	@Override
	public HumanEntity getPlayer() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getPlayer(), HumanEntity.class);
	}

}