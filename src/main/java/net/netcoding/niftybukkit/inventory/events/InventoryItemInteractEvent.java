package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryItemInteractEvent extends InventoryCancellableEvent {

	private final transient PlayerInteractEvent event;

	public InventoryItemInteractEvent(BukkitMojangProfile profile, PlayerInteractEvent event) {
		super(profile);
		this.event = event;
	}

	public ItemStack getItem() {
		return this.event.getItem();
	}

}