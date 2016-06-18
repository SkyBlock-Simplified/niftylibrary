package net.netcoding.nifty.common._new_.api.inventory.events;

import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

abstract class FakeInventoryEvent extends FakeEvent {

	private final Inventory inventory;

	FakeInventoryEvent(BukkitMojangProfile profile, Inventory inventory) {
		super(profile);
		this.inventory = inventory;
	}

	public final Inventory getInventory() {
		return this.inventory;
	}

}