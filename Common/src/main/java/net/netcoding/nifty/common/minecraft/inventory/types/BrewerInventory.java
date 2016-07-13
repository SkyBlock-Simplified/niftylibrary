package net.netcoding.nifty.common.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.block.state.BrewingStand;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface BrewerInventory extends Inventory {

	ItemStack getFuel();

	@Override
	BrewingStand getHolder();

	ItemStack getIngredient();

	void setFuel(ItemStack item);

	void setIngredient(ItemStack item);

}