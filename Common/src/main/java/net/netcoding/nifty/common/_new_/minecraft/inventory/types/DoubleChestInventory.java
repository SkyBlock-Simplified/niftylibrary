package net.netcoding.nifty.common._new_.minecraft.inventory.types;

import net.netcoding.nifty.common._new_.minecraft.block.DoubleChest;
import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;

public interface DoubleChestInventory extends Inventory {

	Inventory getLeftSide();

	Inventory getRightSide();

	@Override
	DoubleChest getHolder();

}