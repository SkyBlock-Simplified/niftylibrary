package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.ShapedRecipe;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

import java.util.Map;

@SuppressWarnings("deprecation")
public final class CraftShapedRecipe extends CraftRecipe implements ShapedRecipe {

	public CraftShapedRecipe(org.bukkit.inventory.ShapedRecipe shapedRecipe) {
		super(shapedRecipe);
	}

	@Override
	public org.bukkit.inventory.ShapedRecipe getHandle() {
		return (org.bukkit.inventory.ShapedRecipe)super.getHandle();
	}

	@Override
	public Map<Character, ItemStack> getIngredientMap() {
		return this.getHandle().getIngredientMap().entrySet().stream().collect(Concurrent.toMap(Map.Entry::getKey, entry -> new CraftItemStack(entry.getValue())));
	}

	@Override
	public String[] getShape() {
		return this.getHandle().getShape();
	}

	@Override
	public ShapedRecipe setIngredient(char key, Material ingredient, int raw) {
		this.getHandle().setIngredient(key, org.bukkit.Material.valueOf(ingredient.name()), raw);
		return this;
	}

	@Override
	public ShapedRecipe shape(String... shape) {
		this.getHandle().shape(shape);
		return this;
	}

}