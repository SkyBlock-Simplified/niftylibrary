package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.PoweredMinecart;

public final class CraftPoweredMinecart extends CraftMinecart implements PoweredMinecart {

	public CraftPoweredMinecart(org.bukkit.entity.minecart.PoweredMinecart poweredMinecart) {
		super(poweredMinecart);
	}

	@Override
	public org.bukkit.entity.minecart.PoweredMinecart getHandle() {
		return (org.bukkit.entity.minecart.PoweredMinecart)super.getHandle();
	}

}