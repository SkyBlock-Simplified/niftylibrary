package net.netcoding.nifty.common._new_.minecraft.block;

import net.netcoding.nifty.common._new_.minecraft.inventory.types.DoubleChestInventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.common._new_.minecraft.region.World;

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
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
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