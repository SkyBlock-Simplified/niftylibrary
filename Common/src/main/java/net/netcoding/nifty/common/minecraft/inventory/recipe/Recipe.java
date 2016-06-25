package net.netcoding.nifty.common.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface Recipe {

	/**
	 * Get the result of this recipe.
	 *
	 * @return The result stack.
	 */
	ItemStack getResult();

}