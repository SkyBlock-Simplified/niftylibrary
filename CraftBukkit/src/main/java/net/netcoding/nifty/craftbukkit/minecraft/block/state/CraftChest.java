package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Chest;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftChest extends CraftBlockState implements Chest {

	public CraftChest(org.bukkit.block.Chest chest) {
		super(chest);
	}

	@Override
	public org.bukkit.block.Chest getHandle() {
		return (org.bukkit.block.Chest)super.getHandle();
	}

	@Override
	public Inventory getBlockInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getBlockInventory());
	}

	@Override
	public Inventory getInventory() {
		return CraftInventory.convertBukkitInventory(this.getHandle().getInventory());
	}

}