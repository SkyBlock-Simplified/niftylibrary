package net.netcoding.nifty.common.api.inventory.events;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

abstract class FakeInventoryEvent extends FakeEvent {

	private final Inventory inventory;

	FakeInventoryEvent(MinecraftMojangProfile profile, Inventory inventory) {
		super(profile);
		this.inventory = inventory;
	}

	public final Inventory getInventory() {
		return this.inventory;
	}

}