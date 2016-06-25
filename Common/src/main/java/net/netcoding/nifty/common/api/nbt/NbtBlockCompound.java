package net.netcoding.nifty.common.api.nbt;

import net.netcoding.nifty.common.minecraft.block.Block;

public abstract class NbtBlockCompound<T extends Block> extends WrappedCompound<T> {

	protected NbtBlockCompound(T block, Object handle) {
		super(block, handle);
	}

	public abstract boolean isTileEntity();

}