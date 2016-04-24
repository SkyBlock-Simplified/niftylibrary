package net.netcoding.niftybukkit.minecraft.inventory.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class InventoryOpenEvent extends InventoryCancellableBukkitEvent {

	public InventoryOpenEvent(BukkitMojangProfile profile, BukkitMojangProfile target, org.bukkit.event.inventory.InventoryEvent event) {
		super(profile, target, event);
	}

}