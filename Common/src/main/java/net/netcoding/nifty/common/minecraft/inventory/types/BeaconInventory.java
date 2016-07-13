package net.netcoding.nifty.common.minecraft.inventory.types;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface BeaconInventory extends Inventory {

	ItemStack getItem();

	void setItem(ItemStack item);

}