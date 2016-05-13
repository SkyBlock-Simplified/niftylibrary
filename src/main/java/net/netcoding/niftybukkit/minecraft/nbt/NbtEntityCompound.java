package net.netcoding.niftybukkit.minecraft.nbt;

import org.bukkit.entity.Entity;

public final class NbtEntityCompound extends WrappedCompound<Entity> {

	NbtEntityCompound(Entity entity, Object handle) {
		super(entity, handle);
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public NbtEntityCompound clone() {
		NbtEntityCompound compound = new NbtEntityCompound(this.getWrapped(), NbtFactory.createRootNativeCompound("tag"));
		compound.putAll(this);
		compound.supported.putAll(this.supported);
		return compound;
	}

	@Override
	protected final void load() {

	}

	@Override
	protected final void save() {

	}

}