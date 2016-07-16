package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Bed;

public final class CraftBed extends CraftMaterialData implements Bed {

	public CraftBed(org.bukkit.material.Bed bed) {
		super(bed);
	}

	@Override
	public Bed clone() {
		return (Bed)super.clone();
	}

}