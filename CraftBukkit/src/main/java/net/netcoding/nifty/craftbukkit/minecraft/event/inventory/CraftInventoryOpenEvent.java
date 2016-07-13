package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryOpenEvent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftInventoryOpenEvent extends CraftInventoryEvent implements InventoryOpenEvent {

	public CraftInventoryOpenEvent(org.bukkit.event.inventory.InventoryOpenEvent inventoryOpenEvent) {
		super(inventoryOpenEvent);
	}

	@Override
	public org.bukkit.event.inventory.InventoryOpenEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryOpenEvent)super.getHandle();
	}

	@Override
	public HumanEntity getPlayer() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getPlayer(), HumanEntity.class);
	}

	@Override
	public boolean isCancelled() {
		return this.getHandle().isCancelled();
	}

	@Override
	public void setCancelled(boolean value) {
		this.getHandle().setCancelled(value);
	}

}