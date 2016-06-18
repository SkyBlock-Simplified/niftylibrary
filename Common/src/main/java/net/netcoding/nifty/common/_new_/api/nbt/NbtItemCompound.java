package net.netcoding.nifty.common._new_.api.nbt;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

public abstract class NbtItemCompound<T extends ItemStack> extends WrappedCompound<T> {

	protected NbtItemCompound(T item, Object handle) {
		super(item, handle);
	}

}