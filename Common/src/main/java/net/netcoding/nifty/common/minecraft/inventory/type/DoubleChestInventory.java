package net.netcoding.nifty.common.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.block.state.DoubleChest;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;

public interface DoubleChestInventory extends Inventory {

	Inventory getLeftSide();

	Inventory getRightSide();

	@Override
	DoubleChest getHolder();

}