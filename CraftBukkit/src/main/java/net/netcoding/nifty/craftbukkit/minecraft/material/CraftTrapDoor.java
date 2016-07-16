package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.TrapDoor;

public final class CraftTrapDoor extends CraftMaterialData implements TrapDoor {

	public CraftTrapDoor(org.bukkit.material.TrapDoor trapDoor) {
		super(trapDoor);
	}

	@Override
	public TrapDoor clone() {
		return (TrapDoor)super.clone();
	}

}