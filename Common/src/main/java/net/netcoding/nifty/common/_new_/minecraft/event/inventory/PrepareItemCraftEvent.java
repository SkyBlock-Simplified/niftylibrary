package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.inventory.types.CraftingInventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.recipe.Recipe;

public interface PrepareItemCraftEvent extends InventoryEvent {

	Recipe getRecipe();

	CraftingInventory getInventory();

	boolean isRepair();

}