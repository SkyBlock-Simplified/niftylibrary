package net.netcoding.nifty.common._new_.minecraft.inventory.types;

import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface HorseInventory extends Inventory {

	ItemStack getArmor();

	ItemStack getSaddle();

	void setArmor(ItemStack item);

	void setSaddle(ItemStack item);

}