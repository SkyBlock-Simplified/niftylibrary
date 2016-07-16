package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Wood;

public class CraftWood extends CraftMaterialData implements Wood {

	public CraftWood(org.bukkit.material.Wood wood) {
		super(wood);
	}

	@Override
	public Wood clone() {
		return (Wood)super.clone();
	}

}