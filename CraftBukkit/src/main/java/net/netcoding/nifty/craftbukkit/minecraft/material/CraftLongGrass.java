package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.LongGrass;

public final class CraftLongGrass extends CraftMaterialData implements LongGrass {

	public CraftLongGrass(org.bukkit.material.LongGrass longGrass) {
		super(longGrass);
	}

	@Override
	public LongGrass clone() {
		return (LongGrass)super.clone();
	}

}