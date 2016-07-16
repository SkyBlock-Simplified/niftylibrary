package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.RedstoneTorch;

public final class CraftRedstoneTorch extends CraftTorch implements RedstoneTorch {

	public CraftRedstoneTorch(org.bukkit.material.RedstoneTorch redstoneTorch) {
		super(redstoneTorch);
	}

	@Override
	public RedstoneTorch clone() {
		return (RedstoneTorch)super.clone();
	}

}