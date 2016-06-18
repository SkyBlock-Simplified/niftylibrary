package net.netcoding.niftybukkit._new_.api.inventory.events;

import net.netcoding.niftybukkit._new_.minecraft.inventory.Inventory;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;

public final class FakeInventoryCloseEvent extends FakeInventoryEvent {

	public FakeInventoryCloseEvent(BukkitMojangProfile profile, Inventory inventory) {
		super(profile, inventory);
	}

}