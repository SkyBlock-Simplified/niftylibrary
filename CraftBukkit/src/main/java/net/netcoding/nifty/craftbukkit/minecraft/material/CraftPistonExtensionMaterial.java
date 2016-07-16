package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.PistonExtensionMaterial;

public final class CraftPistonExtensionMaterial extends CraftMaterialData implements PistonExtensionMaterial {

	public CraftPistonExtensionMaterial(org.bukkit.material.PistonExtensionMaterial pistonExtensionMaterial) {
		super(pistonExtensionMaterial);
	}

	@Override
	public PistonExtensionMaterial clone() {
		return (PistonExtensionMaterial)super.clone();
	}

}