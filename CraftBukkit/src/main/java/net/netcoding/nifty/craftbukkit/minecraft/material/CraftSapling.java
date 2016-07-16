package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Sapling;

public final class CraftSapling extends CraftWood implements Sapling {

	public CraftSapling(org.bukkit.material.Sapling sapling) {
		super(sapling);
	}

	@Override
	public Sapling clone() {
		return (Sapling)super.clone();
	}

}