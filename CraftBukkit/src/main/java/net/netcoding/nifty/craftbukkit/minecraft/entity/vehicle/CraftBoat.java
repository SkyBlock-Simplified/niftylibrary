package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle;

import net.netcoding.nifty.common.minecraft.TreeSpecies;
import net.netcoding.nifty.common.minecraft.entity.vehicle.Boat;

public final class CraftBoat extends CraftVehicle implements Boat {

	public CraftBoat(org.bukkit.entity.Boat boat) {
		super(boat);
	}

	@Override
	public org.bukkit.entity.Boat getHandle() {
		return (org.bukkit.entity.Boat)super.getHandle();
	}

	@Override
	public double getMaxSpeed() {
		return this.getHandle().getMaxSpeed();
	}

	@Override
	public double getOccupiedDeceleration() {
		return this.getHandle().getOccupiedDeceleration();
	}

	@Override
	public double getUnoccupiedDeceleration() {
		return this.getHandle().getUnoccupiedDeceleration();
	}

	@Override
	public TreeSpecies getWoodType() {
		return TreeSpecies.valueOf(this.getHandle().getWoodType().name());
	}

	@Override
	public boolean getWorkOnLand() {
		return this.getHandle().getWorkOnLand();
	}

	@Override
	public void setMaxSpeed(double value) {
		this.getHandle().setMaxSpeed(value);
	}

	@Override
	public void setOccupiedDeceleration(double rate) {
		this.getHandle().setOccupiedDeceleration(rate);
	}

	@Override
	public void setUnoccupiedDeceleration(double rate) {
		this.getHandle().setUnoccupiedDeceleration(rate);
	}

	@Override
	public void setWoodType(TreeSpecies species) {
		this.getHandle().setWoodType(org.bukkit.TreeSpecies.valueOf(species.name()));
	}

	@Override
	public void setWorkOnLand(boolean value) {
		this.getHandle().setWorkOnLand(value);
	}

}