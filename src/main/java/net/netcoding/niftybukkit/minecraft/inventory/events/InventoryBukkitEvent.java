package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.List;

abstract class InventoryBukkitEvent extends InventoryEvent {

	protected final transient org.bukkit.event.inventory.InventoryEvent event;

	InventoryBukkitEvent(BukkitMojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, NiftyBukkit.getMojangRepository().searchByPlayer((Player)event.getInventory().getHolder()));
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