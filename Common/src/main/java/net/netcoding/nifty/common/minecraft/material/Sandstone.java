package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.types.SandstoneType;

public interface Sandstone extends MaterialData {

	@Override
	Sandstone clone();

	default SandstoneType getType() {
		return SandstoneType.getByData(this.getData());
	}

	default void setType(SandstoneType type) {
		this.setData(type.getData());
	}

}