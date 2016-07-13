package net.netcoding.nifty.craftbukkit.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.types.EnchantingInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.CraftInventory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;

public final class CraftEnchantingInventory extends CraftInventory implements EnchantingInventory {

	public CraftEnchantingInventory(org.bukkit.inventory.EnchantingInventory enchantingInventory) {
		super(enchantingInventory);
	}

	@Override
	public org.bukkit.inventory.EnchantingInventory getHandle() {
		return (org.bukkit.inventory.EnchantingInventory)super.getHandle();
	}

	@Override
	public ItemStack getItem() {
		return new CraftItemStack(this.getHandle().getItem());
	}

	@Override
	public ItemStack getSecondary() {
		return new CraftItemStack(this.getHandle().getSecondary());
	}

	@Override
	public void setItem(ItemStack item) {
		this.getHandle().setItem(((CraftItemStack)item).getHandle());
	}

	@Override
	public void setSecondary(ItemStack item) {
		this.getHandle().setSecondary(((CraftItemStack)item).getHandle());
	}

}