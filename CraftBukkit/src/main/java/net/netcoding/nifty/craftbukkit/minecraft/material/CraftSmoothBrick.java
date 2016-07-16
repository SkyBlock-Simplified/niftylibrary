package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.SmoothBrick;

public final class CraftSmoothBrick extends CraftMaterialData implements SmoothBrick {

	public CraftSmoothBrick(org.bukkit.material.SmoothBrick smoothBrick) {
		super(smoothBrick);
	}

	@Override
	public SmoothBrick clone() {
		return (SmoothBrick)super.clone();
	}

}