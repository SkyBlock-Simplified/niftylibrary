package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle;

import net.netcoding.nifty.common.minecraft.entity.vehicle.Vehicle;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public abstract class CraftVehicle extends CraftEntity implements Vehicle {

	protected CraftVehicle(org.bukkit.entity.Vehicle vehicle) {
		super(vehicle);
	}

	@Override
	public org.bukkit.entity.Vehicle getHandle() {
		return (org.bukkit.entity.Vehicle)super.getHandle();
	}

}