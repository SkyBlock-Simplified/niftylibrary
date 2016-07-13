package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.ThrownExpBottle;

public final class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {

	public CraftThrownExpBottle(org.bukkit.entity.ThrownExpBottle thrownExpBottle) {
		super(thrownExpBottle);
	}

	@Override
	public org.bukkit.entity.ThrownExpBottle getHandle() {
		return (org.bukkit.entity.ThrownExpBottle)super.getHandle();
	}

}