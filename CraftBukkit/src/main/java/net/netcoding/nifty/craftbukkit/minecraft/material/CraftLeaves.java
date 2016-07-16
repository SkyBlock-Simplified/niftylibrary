package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Leaves;

public final class CraftLeaves extends CraftWood implements Leaves {

	public CraftLeaves(org.bukkit.material.Leaves leaves) {
		super(leaves);
	}

	@Override
	public Leaves clone() {
		return (Leaves)super.clone();
	}

}