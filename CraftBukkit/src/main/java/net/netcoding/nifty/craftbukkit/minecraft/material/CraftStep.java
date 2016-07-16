package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Step;

public final class CraftStep extends CraftMaterialData implements Step {

	public CraftStep(org.bukkit.material.Step step) {
		super(step);
	}

	@Override
	public Step clone() {
		return (Step)super.clone();
	}

}