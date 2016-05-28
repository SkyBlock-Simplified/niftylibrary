package net.netcoding.niftybukkit.minecraft.nbt;

import org.bukkit.entity.Entity;

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