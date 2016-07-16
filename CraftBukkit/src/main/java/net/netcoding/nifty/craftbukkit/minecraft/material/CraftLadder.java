package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Ladder;

public final class CraftLadder extends CraftMaterialData implements Ladder {

	public CraftLadder(org.bukkit.material.Ladder ladder) {
		super(ladder);
	}

	@Override
	public Ladder clone() {
		return (Ladder)super.clone();
	}

}