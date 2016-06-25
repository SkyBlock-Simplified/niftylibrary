package net.netcoding.nifty.common.minecraft.material;

import java.util.List;

public interface SmoothBrick extends TexturedMaterial {

	@Override
	SmoothBrick clone();

	@Override
	default List<Material> getTextures() {
		return MaterialHelper.getTextures(SmoothBrick.class);
	}

}