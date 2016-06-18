package net.netcoding.nifty.common._new_.api.nbt;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;

public final class NbtEntityCompound extends WrappedCompound<Entity> {

	NbtEntityCompound(Entity entity, Object handle) {
		super(entity, handle);
	}

	@Override
	protected final void load() {

	}

	@Override
	protected final void save() {

	}

}