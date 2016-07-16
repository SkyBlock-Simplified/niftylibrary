package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.SpawnEgg;

public final class CraftSpawnEgg extends CraftMaterialData implements SpawnEgg {

	public CraftSpawnEgg(org.bukkit.material.SpawnEgg spawnEgg) {
		super(spawnEgg);
	}

	@Override
	public SpawnEgg clone() {
		return (SpawnEgg)super.clone();
	}

}