package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.minecraft.event.profile.ProfileEvent;

public abstract class InventoryEvent extends ProfileEvent {

	public InventoryEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	// TODO: Inventory

}