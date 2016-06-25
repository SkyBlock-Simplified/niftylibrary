package net.netcoding.nifty.common._new_.minecraft.inventory.recipe;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface Recipe {

	/**
	 * Get the result of this recipe.
	 *
	 * @return The result stack.
	 */
	ItemStack getResult();

}