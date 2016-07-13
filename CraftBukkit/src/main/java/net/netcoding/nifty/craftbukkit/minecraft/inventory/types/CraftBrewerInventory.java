package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.block.state.BrewingStand;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.types.BrewerInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public final class CraftBrewerInventory extends CraftInventory implements BrewerInventory {

	public CraftBrewerInventory(org.bukkit.inventory.BrewerInventory brewerInventory) {
		super(brewerInventory);
	}

	@Override
	public ItemStack getFuel() {
		return new CraftItemStack(this.getHandle().getFuel());
	}

	@Override
	public org.bukkit.inventory.BrewerInventory getHandle() {
		return (org.bukkit.inventory.BrewerInventory)super.getHandle();
	}

	@Override
	public BrewingStand getHolder() {
		return (BrewingStand)super.getHolder();
	}

	@Override
	public ItemStack getIngredient() {
		return new CraftItemStack(this.getHandle().getIngredient());
	}

	@Override
	public void setFuel(ItemStack item) {
		this.getHandle().setFuel(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setIngredient(ItemStack item) {
		this.getHandle().setIngredient(((CraftItemStack)item).getHandle());
	}

}