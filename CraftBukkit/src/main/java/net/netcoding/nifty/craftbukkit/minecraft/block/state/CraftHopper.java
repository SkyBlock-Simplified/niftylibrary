package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Hopper;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftHopper extends CraftBlockState implements Hopper {

	public CraftHopper(org.bukkit.block.Hopper hopper) {
		super(hopper);
	}

	@Override
	public org.bukkit.block.Hopper getHandle() {
		return (org.bukkit.block.Hopper)super.getHandle();
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

}