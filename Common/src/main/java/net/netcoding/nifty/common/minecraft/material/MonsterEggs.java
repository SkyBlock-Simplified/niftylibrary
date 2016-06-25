package net.netcoding.nifty.common.minecraft.material;

import java.util.List;

public interface MonsterEggs extends TexturedMaterial {

	@Override
	MonsterEggs clone();

	@Override
	default List<Material> getTextures() {
		return MaterialHelper.getTextures(MonsterEggs.class);
	}

}