package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

public interface PlayerSwapHandItemsEvent extends PlayerEvent, Cancellable {

	ItemStack getMainHandItem();

	ItemStack getOffHandItem();

	void setMainHandItem(ItemStack mainHandItem);

	void setOffHandItem(ItemStack offHandItem);

}