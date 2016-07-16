package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Dispenser;

public final class CraftDispenser extends CraftMaterialData implements Dispenser {

	public CraftDispenser(org.bukkit.material.Dispenser dispenser) {
		super(dispenser);
	}

	@Override
	public Dispenser clone() {
		return (Dispenser)super.clone();
	}

}