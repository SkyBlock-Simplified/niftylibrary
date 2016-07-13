package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.LargeFireball;

public final class CraftLargeFireball extends CraftFireball implements LargeFireball {

	public CraftLargeFireball(org.bukkit.entity.LargeFireball largeFireball) {
		super(largeFireball);
	}

	@Override
	public org.bukkit.entity.LargeFireball getHandle() {
		return (org.bukkit.entity.LargeFireball)super.getHandle();
	}

}