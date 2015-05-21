package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class InventoryCloseEvent extends InventoryBukkitEvent {

	public InventoryCloseEvent(BukkitMojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
	}

}