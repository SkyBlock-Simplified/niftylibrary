package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Vine;

public final class CraftVine extends CraftMaterialData implements Vine {

	public CraftVine(org.bukkit.material.Vine vine) {
		super(vine);
	}

	@Override
	public Vine clone() {
		return (Vine)super.clone();
	}

}