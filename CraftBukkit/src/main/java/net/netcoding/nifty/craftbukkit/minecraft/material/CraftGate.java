package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Gate;

public final class CraftGate extends CraftMaterialData implements Gate {

	public CraftGate(org.bukkit.material.Gate gate) {
		super(gate);
	}

	@Override
	public Gate clone() {
		return (Gate)super.clone();
	}

}