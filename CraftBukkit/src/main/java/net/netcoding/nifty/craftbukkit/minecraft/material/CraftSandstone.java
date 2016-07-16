package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Sandstone;

public final class CraftSandstone extends CraftMaterialData implements Sandstone {

	public CraftSandstone(org.bukkit.material.Sandstone sandstone) {
		super(sandstone);
	}

	@Override
	public Sandstone clone() {
		return (Sandstone)super.clone();
	}

}