package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Cauldron;

public final class CraftCauldron extends CraftMaterialData implements Cauldron {

	public CraftCauldron(org.bukkit.material.Cauldron cauldron) {
		super(cauldron);
	}

	@Override
	public Cauldron clone() {
		return (Cauldron)super.clone();
	}

}