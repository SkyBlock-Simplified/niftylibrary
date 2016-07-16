package net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.FurnaceRecipe;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public final class CraftFurnaceRecipe extends CraftRecipe implements FurnaceRecipe {

	public CraftFurnaceRecipe(org.bukkit.inventory.FurnaceRecipe furnaceRecipe) {
		super(furnaceRecipe);
	}

	@Override
	public float getExperience() {
		return this.getHandle().getExperience();
	}

	@Override
	public org.bukkit.inventory.FurnaceRecipe getHandle() {
		return (org.bukkit.inventory.FurnaceRecipe)super.getHandle();
	}

	@Override
	public ItemStack getInput() {
		return new CraftItemStack(this.getHandle().getInput());
	}

	@Override
	public void setExperience(float experience) {
		this.getHandle().setExperience(experience);
	}

	@Override
	public FurnaceRecipe setInput(Material input) {
		this.getHandle().setInput(org.bukkit.Material.valueOf(input.name()));
		return this;
	}

}