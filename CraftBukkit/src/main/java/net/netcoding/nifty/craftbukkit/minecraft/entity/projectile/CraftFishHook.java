package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.FishHook;

public final class CraftFishHook extends CraftProjectile implements FishHook {

	public CraftFishHook(org.bukkit.entity.FishHook fishHook) {
		super(fishHook);
	}

	@Override
	public org.bukkit.entity.FishHook getHandle() {
		return (org.bukkit.entity.FishHook)super.getHandle();
	}

}