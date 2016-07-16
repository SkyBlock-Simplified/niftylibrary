package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.WoodenStep;

public final class CraftWoodenStep extends CraftWood implements WoodenStep {

	public CraftWoodenStep(org.bukkit.material.WoodenStep woodenStep) {
		super(woodenStep);
	}

	@Override
	public WoodenStep clone() {
		return (WoodenStep)super.clone();
	}

}