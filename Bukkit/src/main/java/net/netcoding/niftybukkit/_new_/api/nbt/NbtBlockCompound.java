package net.netcoding.niftybukkit._new_.api.nbt;

import net.netcoding.niftybukkit._new_.minecraft.block.Block;

public abstract class NbtBlockCompound<T extends Block> extends WrappedCompound<T> {

	protected NbtBlockCompound(T block, Object handle) {
		super(block, handle);
	}

	public abstract boolean isTileEntity();

}