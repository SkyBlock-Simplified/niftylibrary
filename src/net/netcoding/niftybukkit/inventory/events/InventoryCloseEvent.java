package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

public class InventoryCloseEvent extends InventoryBukkitEvent {

	public InventoryCloseEvent(MojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
	}

}