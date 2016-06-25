package net.netcoding.nifty.common._new_.minecraft.inventory.types;

import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface CraftingInventory extends Inventory {

	ItemStack[] getMatrix();

	Recipe getRecipe();

	ItemStack getResult();

	void setMatrix(ItemStack[] items);

	void setResult(ItemStack item);

}