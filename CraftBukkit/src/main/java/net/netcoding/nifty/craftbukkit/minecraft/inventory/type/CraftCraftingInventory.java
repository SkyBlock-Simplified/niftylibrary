package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.common.minecraft.inventory.type.CraftingInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe.CraftRecipe;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Arrays;

public final class CraftCraftingInventory extends CraftInventory implements CraftingInventory {

	public CraftCraftingInventory(org.bukkit.inventory.CraftingInventory craftingInventory) {
		super(craftingInventory);
	}

	@Override
	public org.bukkit.inventory.CraftingInventory getHandle() {
		return (org.bukkit.inventory.CraftingInventory)super.getHandle();
	}

	@Override
	public ItemStack[] getMatrix() {
		return Arrays.stream(this.getHandle().getMatrix()).map(CraftItemStack::new).toArray(CraftItemStack[]::new);
	}

	@Override
	public Recipe getRecipe() {
		return CraftRecipe.convertBukkitRecipe(this.getHandle().getRecipe());
	}

	@Override
	public ItemStack getResult() {
		return new CraftItemStack(this.getHandle().getResult());
	}

	@Override
	public void setMatrix(ItemStack[] items) {
		this.getHandle().setMatrix(Arrays.stream(items).map(CraftConverter::toBukkitItem).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setResult(ItemStack item) {
		this.getHandle().setResult(CraftConverter.toBukkitItem(item));
	}

}