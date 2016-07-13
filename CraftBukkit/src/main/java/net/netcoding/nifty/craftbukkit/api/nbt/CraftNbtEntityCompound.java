package net.netcoding.nifty.craftbukkit.api.nbt;

import net.netcoding.nifty.common.api.nbt.NbtEntityCompound;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftNbtEntityCompound extends NbtEntityCompound<CraftEntity> {

	public CraftNbtEntityCompound(CraftEntity entity, Object handle) {
		super(entity, handle);
		this.load();
	}

	@Override
	protected void load() {

	}

	@Override
	protected void save() {

	}

}