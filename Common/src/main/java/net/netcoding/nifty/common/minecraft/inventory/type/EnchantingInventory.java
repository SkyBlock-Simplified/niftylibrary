package net.netcoding.nifty.common.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface EnchantingInventory extends Inventory {

	ItemStack getItem();

	ItemStack getSecondary();

	void setItem(ItemStack item);

	void setSecondary(ItemStack item);

}