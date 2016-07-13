package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.EnderSignal;

public final class CraftEnderSignal extends CraftEntity implements EnderSignal {

	public CraftEnderSignal(org.bukkit.entity.EnderSignal enderSignal) {
		super(enderSignal);
	}

	@Override
	public org.bukkit.entity.EnderSignal getHandle() {
		return (org.bukkit.entity.EnderSignal)super.getHandle();
	}

}