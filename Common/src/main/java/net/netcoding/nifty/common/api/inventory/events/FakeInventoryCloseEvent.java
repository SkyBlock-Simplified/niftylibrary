package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public final class FakeInventoryCloseEvent extends FakeInventoryEvent {

	public FakeInventoryCloseEvent(MinecraftMojangProfile profile, Inventory inventory) {
		super(profile, inventory);
	}

}