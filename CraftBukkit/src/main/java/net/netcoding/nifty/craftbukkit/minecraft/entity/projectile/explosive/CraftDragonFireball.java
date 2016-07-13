package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.DragonFireball;

public final class CraftDragonFireball extends CraftFireball implements DragonFireball {

	public CraftDragonFireball(org.bukkit.entity.DragonFireball dragonFireball) {
		super(dragonFireball);
	}

	@Override
	public org.bukkit.entity.DragonFireball getHandle() {
		return (org.bukkit.entity.DragonFireball)super.getHandle();
	}

}