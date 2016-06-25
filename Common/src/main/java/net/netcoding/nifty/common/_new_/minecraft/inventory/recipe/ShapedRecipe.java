package net.netcoding.nifty.common._new_.minecraft.inventory.recipe;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common._new_.minecraft.material.MaterialData;

import java.util.Map;

/**
 * Represents a shaped (ie. normal) crafting recipe.
 */
public interface ShapedRecipe extends Recipe {

	/**
	 * Get a copy of the ingredients map.
	 *
	 * @return The mapping of character to ingredients.
	 */
	Map<Character, ItemStack> getIngredientMap();

	/**
	 * Get the shape.
	 *
	 * @return The recipe's shape.
	 */
	String[] getShape();

	/**
	 * Sets the material that a character in the recipe shape refers to.
	 *
	 * @param key The character that represents the ingredient in the shape.
	 * @param ingredient The ingredient.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapedRecipe setIngredient(char key, MaterialData ingredient) {
		return this.setIngredient(key, ingredient.getItemType(), ingredient.getData());
	}

	/**
	 * Sets the material that a character in the recipe shape refers to.
	 *
	 * @param key The character that represents the ingredient in the shape.
	 * @param ingredient The ingredient.
	 * @return The changed recipe, so you can chain calls.
	 */
	default ShapedRecipe setIngredient(char key, Material ingredient) {
		return this.setIngredient(key, ingredient, 0);
	}

	/**
	 * Sets the material that a character in the recipe shape refers to.
	 *
	 * @param key The character that represents the ingredient in the shape.
	 * @param ingredient The ingredient.
	 * @param raw The raw material data as an integer.
	 * @return The changed recipe, so you can chain calls.
	 */
	ShapedRecipe setIngredient(char key, Material ingredient, int raw);

	/**
	 * Set the shape of this recipe to the specified rows. Each character
	 * represents a different ingredient; exactly what each character
	 * represents is set separately. The first row supplied corresponds with
	 * the upper most part of the recipe on the workbench e.g. if all three
	 * rows are supplies the first string represents the top row on the
	 * workbench.
	 *
	 * @param shape The rows of the recipe (up to 3 rows).
	 * @return The changed recipe, so you can chain calls.
	 */
	ShapedRecipe shape(String... shape);

}