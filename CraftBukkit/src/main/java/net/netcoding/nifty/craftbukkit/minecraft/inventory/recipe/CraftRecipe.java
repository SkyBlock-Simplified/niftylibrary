package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public class CraftRecipe implements Recipe {

	private final org.bukkit.inventory.Recipe recipe;

	public CraftRecipe(org.bukkit.inventory.Recipe recipe) {
		this.recipe = recipe;
	}

	public static Recipe convertBukkitRecipe(org.bukkit.inventory.Recipe bukkitRecipe) {
		return convertBukkitRecipe(bukkitRecipe, Recipe.class);
	}

	public static <T extends Recipe> T convertBukkitRecipe(org.bukkit.inventory.Recipe bukkitRecipe, Class<T> recipeType) {
		CraftRecipeType type = CraftRecipeType.getByBukkitClass(bukkitRecipe.getClass());
		return recipeType.cast(new Reflection(type.getClazz()).newInstance(bukkitRecipe));
	}

	public org.bukkit.inventory.Recipe getHandle() {
		return this.recipe;
	}

	@Override
	public ItemStack getResult() {
		return new CraftItemStack(this.getHandle().getResult());
	}

}