package net.netcoding.nifty.craftbukkit.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.event.inventory.PrepareItemCraftEvent;
import net.netcoding.nifty.common.minecraft.inventory.recipe.Recipe;
import net.netcoding.nifty.common.minecraft.inventory.type.CraftingInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe.CraftRecipe;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.type.CraftCraftingInventory;

public class CraftPrepareItemCraftEvent extends CraftInventoryEvent implements PrepareItemCraftEvent {

	public CraftPrepareItemCraftEvent(org.bukkit.event.inventory.PrepareItemCraftEvent prepareItemCraftEvent) {
		super(prepareItemCraftEvent);
	}

	@Override
	public org.bukkit.event.inventory.PrepareItemCraftEvent getHandle() {
		return (org.bukkit.event.inventory.PrepareItemCraftEvent)super.getHandle();
	}

	@Override
	public Recipe getRecipe() {
		return CraftRecipe.convertBukkitRecipe(this.getHandle().getRecipe());
	}

	@Override
	public CraftingInventory getInventory() {
		return new CraftCraftingInventory(this.getHandle().getInventory());
	}

	@Override
	public boolean isRepair() {
		return this.getHandle().isRepair();
	}

}