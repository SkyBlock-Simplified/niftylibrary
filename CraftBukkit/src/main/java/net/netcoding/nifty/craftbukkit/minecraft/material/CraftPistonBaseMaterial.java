package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.PistonBaseMaterial;

public final class CraftPistonBaseMaterial extends CraftMaterialData implements PistonBaseMaterial {

	public CraftPistonBaseMaterial(org.bukkit.material.PistonBaseMaterial pistonBaseMaterial) {
		super(pistonBaseMaterial);
	}

	@Override
	public PistonBaseMaterial clone() {
		return (PistonBaseMaterial)super.clone();
	}

}