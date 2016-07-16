package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Crops;

public final class CraftCrops extends CraftMaterialData implements Crops {

	public CraftCrops(org.bukkit.material.Crops crops) {
		super(crops);
	}

	@Override
	public Crops clone() {
		return (Crops)super.clone();
	}

}