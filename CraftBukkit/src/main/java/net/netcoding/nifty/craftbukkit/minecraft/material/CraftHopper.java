package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Hopper;

public final class CraftHopper extends CraftMaterialData implements Hopper {

	public CraftHopper(org.bukkit.material.Hopper hopper) {
		super(hopper);
	}

	@Override
	public Hopper clone() {
		return (Hopper)super.clone();
	}

}