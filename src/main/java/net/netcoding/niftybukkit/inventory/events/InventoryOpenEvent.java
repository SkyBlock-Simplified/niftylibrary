package net.netcoding.niftybukkit.inventory.events;

import net.netcoding.niftycore.mojang.MojangProfile;

public class InventoryOpenEvent extends InventoryCancellableBukkitEvent {

	public InventoryOpenEvent(MojangProfile profile, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, event);
	}

}