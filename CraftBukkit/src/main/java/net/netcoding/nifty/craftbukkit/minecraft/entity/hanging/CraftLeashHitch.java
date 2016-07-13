package net.netcoding.nifty.craftbukkit.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.entity.hanging.LeashHitch;

public final class CraftLeashHitch extends CraftHanging implements LeashHitch {

	public CraftLeashHitch(org.bukkit.entity.LeashHitch leashHitch) {
		super(leashHitch);
	}

	@Override
	public org.bukkit.entity.LeashHitch getHandle() {
		return (org.bukkit.entity.LeashHitch)super.getHandle();
	}

}