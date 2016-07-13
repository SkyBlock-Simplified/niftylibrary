package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Beacon;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftBeacon extends CraftBlockState implements Beacon {

	public CraftBeacon(org.bukkit.block.Beacon beacon) {
		super(beacon);
	}

	@Override
	public org.bukkit.block.Beacon getHandle() {
		return (org.bukkit.block.Beacon)super.getHandle();
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

}