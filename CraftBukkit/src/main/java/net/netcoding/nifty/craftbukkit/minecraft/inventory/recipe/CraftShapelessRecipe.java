package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.ShapelessRecipe;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

import java.util.List;

@SuppressWarnings("deprecation")
public final class CraftShapelessRecipe extends CraftRecipe implements ShapelessRecipe {

	public CraftShapelessRecipe(org.bukkit.inventory.ShapelessRecipe shapelessRecipe) {
		super(shapelessRecipe);
	}

	@Override
	public org.bukkit.inventory.ShapelessRecipe getHandle() {
		return (org.bukkit.inventory.ShapelessRecipe)super.getHandle();
	}

	@Override
	public ShapelessRecipe addIngredient(int count, Material ingredient, int rawdata) {
		this.getHandle().addIngredient(count, org.bukkit.Material.valueOf(ingredient.name()), rawdata);
		return this;
	}

	@Override
	public List<ItemStack> getIngredientList() {
		return this.getHandle().getIngredientList().stream().map(CraftItemStack::new).collect(Concurrent.toList());
	}

	@Override
	public ShapelessRecipe removeIngredient(int count, Material ingredient, int rawdata) {
		this.getHandle().removeIngredient(count, org.bukkit.Material.valueOf(ingredient.name()), rawdata);
		return this;
	}

}