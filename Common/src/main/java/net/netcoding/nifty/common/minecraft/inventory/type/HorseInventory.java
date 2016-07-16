package net.netcoding.nifty.common.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface HorseInventory extends Inventory {

	ItemStack getArmor();

	ItemStack getSaddle();

	void setArmor(ItemStack item);

	void setSaddle(ItemStack item);

}