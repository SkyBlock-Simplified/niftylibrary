package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Door;

public final class CraftDoor extends CraftMaterialData implements Door {

	public CraftDoor(org.bukkit.material.Door door) {
		super(door);
	}

	@Override
	public Door clone() {
		return (Door)super.clone();
	}

}