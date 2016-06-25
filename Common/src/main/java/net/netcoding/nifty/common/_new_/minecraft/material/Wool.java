package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.core.api.DyeColor;

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