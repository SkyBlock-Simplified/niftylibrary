package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.inventory.types.DoubleChestInventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;

public final class DoubleChest implements InventoryHolder {

	private DoubleChestInventory inventory;

	public DoubleChest(DoubleChestInventory chest) {
		this.inventory = chest;
	}

	@Override
	public DoubleChestInventory getInventory() {
		return this.inventory;
	}

	public InventoryHolder getLeftSide() {
		return this.getInventory().getLeftSide().getHolder();
	}

	public Location getLocation() {
		return Location.of(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}

	public InventoryHolder getRightSide() {
		return this.getInventory().getRightSide().getHolder();
	}

	public World getWorld() {
		return ((Chest)getLeftSide()).getWorld();
	}

	public double getX() {
		return 0.5 * (((Chest)getLeftSide()).getLocation().getX() + ((Chest)getRightSide()).getLocation().getX());
	}

	public double getY() {
		return 0.5 * (((Chest)getLeftSide()).getLocation().getY() + ((Chest)getRightSide()).getLocation().getY());
	}

	public double getZ() {
		return 0.5 * (((Chest)getLeftSide()).getLocation().getZ() + ((Chest)getRightSide()).getLocation().getZ());
	}

}