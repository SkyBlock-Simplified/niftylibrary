package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.PressurePlate;

public final class CraftPressurePlate extends CraftMaterialData implements PressurePlate {

	public CraftPressurePlate(org.bukkit.material.PressurePlate pressurePlate) {
		super(pressurePlate);
	}

	@Override
	public PressurePlate clone() {
		return (PressurePlate)super.clone();
	}

}