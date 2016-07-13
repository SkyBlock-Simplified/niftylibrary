package net.netcoding.nifty.common.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;

public interface MerchantInventory extends Inventory {

	MerchantRecipe getSelectedRecipe();

	int getSelectedRecipeIndex();

}