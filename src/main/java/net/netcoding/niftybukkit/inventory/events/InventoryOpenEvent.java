package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class InventoryOpenEvent extends InventoryCancellableBukkitEvent {

	public InventoryOpenEvent(BukkitMojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
	}

}