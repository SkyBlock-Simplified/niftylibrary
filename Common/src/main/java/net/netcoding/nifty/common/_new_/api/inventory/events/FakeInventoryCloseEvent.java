package net.netcoding.nifty.common._new_.api.inventory.events;

import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;

public final class FakeInventoryCloseEvent extends FakeInventoryEvent {

	public FakeInventoryCloseEvent(BukkitMojangProfile profile, Inventory inventory) {
		super(profile, inventory);
	}

}