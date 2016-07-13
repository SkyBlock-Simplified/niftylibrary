package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.common.minecraft.inventory.types.CraftingInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

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
		return null; // TODO
	}

	@Override
	public ItemStack getResult() {
		return new CraftItemStack(this.getHandle().getResult());
	}

	@Override
	public void setMatrix(ItemStack[] items) {
		this.getHandle().setMatrix(Arrays.stream(items).map(item -> ((CraftItemStack)item).getHandle()).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setResult(ItemStack item) {
		this.getHandle().setResult(((CraftItemStack)item).getHandle());
	}

}