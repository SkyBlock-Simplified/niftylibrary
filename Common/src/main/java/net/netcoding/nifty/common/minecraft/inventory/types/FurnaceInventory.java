package net.netcoding.nifty.common.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.block.Furnace;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface FurnaceInventory extends Inventory {

	ItemStack getFuel();

	@Override
	Furnace getHolder();

	ItemStack getResult();

	ItemStack getSmelting();

	void setFuel(ItemStack item);

	void setResult(ItemStack item);

	void setSmelting(ItemStack item);

}