package net.netcoding.nifty.common._new_.minecraft.inventory.types;

import net.netcoding.nifty.common._new_.minecraft.block.BrewingStand;
import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface BrewerInventory extends Inventory {

	ItemStack getFuel();

	@Override
	BrewingStand getHolder();

	ItemStack getIngredient();

	void setFuel(ItemStack item);

	void setIngredient(ItemStack item);

}