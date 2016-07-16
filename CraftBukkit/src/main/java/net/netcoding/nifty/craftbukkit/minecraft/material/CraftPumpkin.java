package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Pumpkin;

public final class CraftPumpkin extends CraftMaterialData implements Pumpkin {

	public CraftPumpkin(org.bukkit.material.Pumpkin pumpkin) {
		super(pumpkin);
	}

	@Override
	public Pumpkin clone() {
		return (Pumpkin)super.clone();
	}

}