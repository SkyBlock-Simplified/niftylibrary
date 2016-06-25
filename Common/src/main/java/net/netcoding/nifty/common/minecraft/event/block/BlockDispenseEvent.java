package net.netcoding.nifty.common.minecraft.event.block;

import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.core.util.misc.Vector;

public interface BlockDispenseEvent extends BlockEvent, Cancellable {

	ItemStack getItemStack();

	Vector getVelocity();

	void setItem(ItemStack item);

	void setVelocity(Vector velocity);

}