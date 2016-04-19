package net.netcoding.niftybukkit.minecraft.inventory.events;

import java.util.List;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

abstract class InventoryBukkitEvent extends InventoryEvent {

	protected final transient org.bukkit.event.inventory.InventoryEvent event;

	InventoryBukkitEvent(BukkitMojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile);
		this.event = event;
	}

	public Inventory getInventory() {
		return this.event.getInventory();
	}

	public InventoryView getView() {
		return this.event.getView();
	}

	public List<HumanEntity> getViewers() {
		return this.event.getViewers();
	}

}