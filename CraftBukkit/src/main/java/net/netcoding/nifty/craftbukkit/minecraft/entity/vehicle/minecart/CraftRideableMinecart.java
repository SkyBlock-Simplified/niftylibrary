package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.RideableMinecart;

public final class CraftRideableMinecart extends CraftMinecart implements RideableMinecart {

	public CraftRideableMinecart(org.bukkit.entity.minecart.RideableMinecart rideableMinecart) {
		super(rideableMinecart);
	}

	@Override
	public org.bukkit.entity.minecart.RideableMinecart getHandle() {
		return (org.bukkit.entity.minecart.RideableMinecart)super.getHandle();
	}

}