package net.netcoding.nifty.craftbukkit.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;
import net.netcoding.nifty.common.minecraft.inventory.type.MerchantInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.recipe.CraftRecipe;

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
		return CraftRecipe.convertBukkitRecipe(this.getHandle().getSelectedRecipe(), MerchantRecipe.class);
	}

	@Override
	public int getSelectedRecipeIndex() {
		return this.getHandle().getSelectedRecipeIndex();
	}

}