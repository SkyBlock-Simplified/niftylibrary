package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.EnderPearl;

public final class CraftEnderPearl extends CraftProjectile implements EnderPearl {

	public CraftEnderPearl(org.bukkit.entity.EnderPearl enderPearl) {
		super(enderPearl);
	}

	@Override
	public org.bukkit.entity.EnderPearl getHandle() {
		return (org.bukkit.entity.EnderPearl)super.getHandle();
	}

}