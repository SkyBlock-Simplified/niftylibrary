package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.Snowball;

public final class CraftSnowball extends CraftProjectile implements Snowball {

	public CraftSnowball(org.bukkit.entity.Snowball snowball) {
		super(snowball);
	}

	@Override
	public org.bukkit.entity.Snowball getHandle() {
		return (org.bukkit.entity.Snowball)super.getHandle();
	}

}