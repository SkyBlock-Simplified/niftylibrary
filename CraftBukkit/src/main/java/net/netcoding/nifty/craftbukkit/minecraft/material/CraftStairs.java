package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Stairs;

public final class CraftStairs extends CraftMaterialData implements Stairs {

	public CraftStairs(org.bukkit.material.Stairs stairs) {
		super(stairs);
	}

	@Override
	public Stairs clone() {
		return (Stairs)super.clone();
	}

}