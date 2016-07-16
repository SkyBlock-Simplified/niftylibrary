package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Rails;

public class CraftRails extends CraftMaterialData implements Rails {

	public CraftRails(org.bukkit.material.Rails rails) {
		super(rails);
	}

	@Override
	public Rails clone() {
		return (Rails)super.clone();
	}

}