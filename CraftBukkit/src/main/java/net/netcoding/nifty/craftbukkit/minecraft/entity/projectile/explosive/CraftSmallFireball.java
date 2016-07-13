package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.SmallFireball;

public final class CraftSmallFireball extends CraftFireball implements SmallFireball {

	public CraftSmallFireball(org.bukkit.entity.SmallFireball smallFireball) {
		super(smallFireball);
	}

	@Override
	public org.bukkit.entity.SmallFireball getHandle() {
		return (org.bukkit.entity.SmallFireball)super.getHandle();
	}

}