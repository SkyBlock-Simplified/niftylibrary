package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Lever;

public final class CraftLever extends CraftMaterialData implements Lever {

	public CraftLever(org.bukkit.material.Lever lever) {
		super(lever);
	}

	@Override
	public Lever clone() {
		return (Lever)super.clone();
	}

}