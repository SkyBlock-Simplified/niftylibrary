package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;
import net.netcoding.nifty.common.minecraft.inventory.types.MerchantInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;

public final class CraftMerchantInventory extends CraftInventory implements MerchantInventory {

	public CraftMerchantInventory(org.bukkit.inventory.MerchantInventory merchantInventory) {
		super(merchantInventory);
	}

	@Override
	public org.bukkit.inventory.MerchantInventory getHandle() {
		return (org.bukkit.inventory.MerchantInventory)super.getHandle();
	}

	@Override
	public MerchantRecipe getSelectedRecipe() {
		return null; // TODO
	}

	@Override
	public int getSelectedRecipeIndex() {
		return this.getHandle().getSelectedRecipeIndex();
	}

}