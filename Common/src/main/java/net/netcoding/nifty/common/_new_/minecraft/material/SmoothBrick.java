package net.netcoding.nifty.common._new_.minecraft.material;

import java.util.List;

public interface SmoothBrick extends TexturedMaterial {

	@Override
	SmoothBrick clone();

	@Override
	default List<Material> getTextures() {
		return MaterialHelper.getTextures(SmoothBrick.class);
	}

}