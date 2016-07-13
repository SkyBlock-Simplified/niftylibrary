package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.potion;

import net.netcoding.nifty.common.minecraft.entity.projectile.potion.SplashPotion;

public final class CraftSplashPotion extends CraftThrownPotion implements SplashPotion {

	public CraftSplashPotion(org.bukkit.entity.SplashPotion splashPotion) {
		super(splashPotion);
	}

	@Override
	public org.bukkit.entity.SplashPotion getHandle() {
		return (org.bukkit.entity.SplashPotion)super.getHandle();
	}

}