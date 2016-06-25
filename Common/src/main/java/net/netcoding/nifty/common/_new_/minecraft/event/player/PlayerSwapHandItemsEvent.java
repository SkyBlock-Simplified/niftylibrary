package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public interface PlayerSwapHandItemsEvent extends PlayerEvent, Cancellable {

	ItemStack getMainHandItem();

	ItemStack getOffHandItem();

	void setMainHandItem(ItemStack mainHandItem);

	void setOffHandItem(ItemStack offHandItem);

}