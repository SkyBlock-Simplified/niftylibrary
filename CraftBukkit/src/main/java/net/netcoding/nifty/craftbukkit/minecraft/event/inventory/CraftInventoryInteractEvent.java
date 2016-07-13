package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.EventResult;
import net.netcoding.nifty.common.minecraft.event.inventory.InventoryInteractEvent;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftInventoryInteractEvent extends CraftInventoryEvent implements InventoryInteractEvent {

	public CraftInventoryInteractEvent(org.bukkit.event.inventory.InventoryInteractEvent inventoryInteractEvent) {
		super(inventoryInteractEvent);
	}

	@Override
	public org.bukkit.event.inventory.InventoryInteractEvent getHandle() {
		return (org.bukkit.event.inventory.InventoryInteractEvent)super.getHandle();
	}

	@Override
	public EventResult getResult() {
		return EventResult.valueOf(this.getHandle().getResult().name());
	}

	@Override
	public HumanEntity getWhoClicked() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getWhoClicked(), HumanEntity.class);
	}

	@Override
	public void setResult(EventResult result) {
		this.getHandle().setResult(org.bukkit.event.Event.Result.valueOf(result.name()));
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