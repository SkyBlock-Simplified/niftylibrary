package net.netcoding.nifty.common._new_.minecraft.inventory.recipe;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common._new_.minecraft.material.MaterialData;

public interface FurnaceRecipe extends Recipe {

	/**
	 * Sets the input of this furnace recipe.
	 *
	 * @param input The input material.
	 */
	default FurnaceRecipe setInput(MaterialData input) {
		return this.setInput(input.getItemType());
	}

	/**
	 * Sets the input of this furnace recipe.
	 *
	 * @param input The input material.
	 */
	FurnaceRecipe setInput(Material input);

	/**
	 * Get the input material.
	 *
	 * @return The input material.
	 */
	ItemStack getInput();

	/**
	 * Sets the experience given by this recipe.
	 *
	 * @param experience The experience level.
	 */
	void setExperience(float experience);

	/**
	 * Get the experience given by this recipe.
	 *
	 * @return Experience level.
	 */
	float getExperience();

}