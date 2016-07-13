package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.Egg;

public final class CraftEgg extends CraftProjectile implements Egg {

	public CraftEgg(org.bukkit.entity.Egg egg) {
		super(egg);
	}

	@Override
	public org.bukkit.entity.Egg getHandle() {
		return (org.bukkit.entity.Egg)super.getHandle();
	}

}