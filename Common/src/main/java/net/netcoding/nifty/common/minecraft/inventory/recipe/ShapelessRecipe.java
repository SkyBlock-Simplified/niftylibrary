package net.netcoding.nifty.common.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;

import java.util.List;

/**
 * Represents a shapeless recipe, where the arrangement of the ingredients on
 * the crafting grid does not matter.
 */
public interface ShapelessRecipe extends Recipe {

	/**
	 * Adds the specified ingredient.
	 *
	 * @param ingredient The ingredient to add.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapelessRecipe addIngredient(MaterialData ingredient) {
		return this.addIngredient(1, ingredient);
	}

	/**
	 * Adds the specified ingredient.
	 *
	 * @param ingredient The ingredient to add.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapelessRecipe addIngredient(Material ingredient) {
		return this.addIngredient(1, ingredient, 0);
	}

	/**
	 * Adds the specified ingredient.
	 *
	 * @param ingredient The ingredient to add.
	 * @param rawdata The data value, or -1 to allow any data value.
	 * @return The changed recipe, so you can chain calls.
	 * @deprecated Magic value
	 */
	@Deprecated
	default ShapelessRecipe addIngredient(Material ingredient, int rawdata) {
		return this.addIngredient(1, ingredient, rawdata);
	}

	/**
	 * Adds multiples of the specified ingredient.
	 *
	 * @param count How many to add (can't be more than 9!)
	 * @param ingredient The ingredient to add.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapelessRecipe addIngredient(int count, MaterialData ingredient) {
		return this.addIngredient(count, ingredient.getItemType(), ingredient.getData());
	}

	/**
	 * Adds multiples of the specified ingredient.
	 *
	 * @param count How many to add (can't be more than 9!)
	 * @param ingredient The ingredient to add.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapelessRecipe addIngredient(int count, Material ingredient) {
		return this.addIngredient(count, ingredient, 0);
	}

	/**
	 * Adds multiples of the specified ingredient.
	 *
	 * @param count How many to add (can't be more than 9!)
	 * @param ingredient The ingredient to add.
	 * @param rawdata The data value, or -1 to allow any data value.
	 * @return The changed recipe, so you can chain calls.
	 */
	ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata);

	/**
	 * Get the list of ingredients used for this recipe.
	 *
	 * @return The input list
	 */
	List<ItemStack> getIngredientList();

	/**
	 * Removes an ingredient from the list. If the ingredient occurs multiple
	 * times, only one instance of it is removed. Only removes exact matches,
	 * with a data value of 0.
	 *
	 * @param ingredient The ingredient to remove
	 * @return The changed recipe.
	 */
	default ShapelessRecipe removeIngredient(Material ingredient) {
		return this.removeIngredient(ingredient, 0);
	}

	/**
	 * Removes an ingredient from the list. If the ingredient occurs multiple
	 * times, only one instance of it is removed. If the data value is -1,
	 * only ingredients with a -1 data value will be removed.
	 *
	 * @param ingredient The ingredient to remove
	 * @return The changed recipe.
	 */
	default ShapelessRecipe removeIngredient(MaterialData ingredient) {
		return this.removeIngredient(ingredient.getItemType(), ingredient.getData());
	}

	/**
	 * Removes multiple instances of an ingredient from the list. If there are
	 * less instances then specified, all will be removed. Only removes exact
	 * matches, with a data value of 0.
	 *
	 * @param count The number of copies to remove.
	 * @param ingredient The ingredient to remove
	 * @return The changed recipe.
	 */
	default ShapelessRecipe removeIngredient(int count, Material ingredient) {
		return this.removeIngredient(count, ingredient, 0);
	}

	/**
	 * Removes multiple instances of an ingredient from the list. If there are
	 * less instances then specified, all will be removed. If the data value
	 * is -1, only ingredients with a -1 data value will be removed.
	 *
	 * @param count The number of copies to remove.
	 * @param ingredient The ingredient to remove.
	 * @return The changed recipe.
	 */
	default ShapelessRecipe removeIngredient(int count, MaterialData ingredient) {
		return this.removeIngredient(count, ingredient.getItemType(), ingredient.getData());
	}

	/**
	 * Removes an ingredient from the list. If the ingredient occurs multiple
	 * times, only one instance of it is removed. If the data value is -1,
	 * only ingredients with a -1 data value will be removed.
	 *
	 * @param ingredient The ingredient to remove
	 * @param rawdata The data value;
	 * @return The changed recipe.
	 */
	default ShapelessRecipe removeIngredient(Material ingredient, int rawdata) {
		return this.removeIngredient(1, ingredient, rawdata);
	}

	/**
	 * Removes multiple instances of an ingredient from the list. If there are
	 * less instances then specified, all will be removed. If the data value
	 * is -1, only ingredients with a -1 data value will be removed.
	 *
	 * @param count The number of copies to remove.
	 * @param ingredient The ingredient to remove.
	 * @param rawdata The data value.
	 * @return The changed recipe.
	 */
	ShapelessRecipe removeIngredient(int count, Material ingredient, int rawdata);

}