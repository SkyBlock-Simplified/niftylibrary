package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.core.api.color.DyeColor;

public interface Wool extends MaterialData, Colorable {

	@Override
	Wool clone();

	@Override
	default DyeColor getColor() {
		return DyeColor.getByWoolData(this.getData());
	}

	@Override
	default void setColor(DyeColor color) {
		this.setData(color.getWoolData());
	}

}