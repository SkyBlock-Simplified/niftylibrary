package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Wool;

public final class CraftWool extends CraftMaterialData implements Wool {

	public CraftWool(org.bukkit.material.Wool wool) {
		super(wool);
	}

	@Override
	public Wool clone() {
		return (Wool)super.clone();
	}

}