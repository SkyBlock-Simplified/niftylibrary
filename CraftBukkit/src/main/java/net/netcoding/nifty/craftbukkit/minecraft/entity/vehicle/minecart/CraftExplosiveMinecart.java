package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.ExplosiveMinecart;

public final class CraftExplosiveMinecart extends CraftMinecart implements ExplosiveMinecart {

	public CraftExplosiveMinecart(org.bukkit.entity.minecart.ExplosiveMinecart explosiveMinecart) {
		super(explosiveMinecart);
	}

	@Override
	public org.bukkit.entity.minecart.ExplosiveMinecart getHandle() {
		return (org.bukkit.entity.minecart.ExplosiveMinecart)super.getHandle();
	}

}