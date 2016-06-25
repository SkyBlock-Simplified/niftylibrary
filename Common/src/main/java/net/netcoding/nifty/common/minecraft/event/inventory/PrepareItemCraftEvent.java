package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.inventory.types.CraftingInventory;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;

public interface PrepareItemCraftEvent extends InventoryEvent {

	Recipe getRecipe();

	CraftingInventory getInventory();

	boolean isRepair();

}