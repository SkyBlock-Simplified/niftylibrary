package net.netcoding.nifty.common.api.nbt;

import net.netcoding.nifty.common.minecraft.entity.Entity;

public abstract class NbtEntityCompound<T extends Entity> extends WrappedCompound<T> {

	protected NbtEntityCompound(T entity, Object handle) {
		super(entity, handle);
	}

}