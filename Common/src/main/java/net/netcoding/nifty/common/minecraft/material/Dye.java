package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.core.api.DyeColor;

public interface Dye extends MaterialData, Colorable {

	@Override
	Dye clone();

	@Override
	default DyeColor getColor() {
		return DyeColor.getByDyeData(this.getData());
	}

	@Override
	default void setColor(DyeColor color) {
		this.setData(color.getDyeData());
	}

}