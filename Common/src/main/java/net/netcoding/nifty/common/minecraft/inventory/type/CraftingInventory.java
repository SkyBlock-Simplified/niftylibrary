package net.netcoding.nifty.common.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface CraftingInventory extends Inventory {

	ItemStack[] getMatrix();

	Recipe getRecipe();

	ItemStack getResult();

	void setMatrix(ItemStack[] items);

	void setResult(ItemStack item);

}