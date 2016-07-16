package net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.minecart.Minecart;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.entity.vehicle.CraftVehicle;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {

	public CraftMinecart(org.bukkit.entity.Minecart minecart) {
		super(minecart);
	}

	@Override
	public double getDamage() {
		return this.getHandle().getDamage();
	}

	@Override
	public Vector getDerailedVelocityMod() {
		return CraftConverter.fromBukkitVector(this.getHandle().getDerailedVelocityMod());
	}

	@Override
	public MaterialData getDisplayBlock() {
		return CraftConverter.fromBukkitData(this.getHandle().getDisplayBlock());
	}

	@Override
	public int getDisplayBlockOffset() {
		return this.getHandle().getDisplayBlockOffset();
	}

	@Override
	public Vector getFlyingVelocityMod() {
		return CraftConverter.fromBukkitVector(this.getHandle().getFlyingVelocityMod());
	}

	@Override
	public org.bukkit.entity.Minecart getHandle() {
		return (org.bukkit.entity.Minecart)super.getHandle();
	}

	@Override
	public double getMaxSpeed() {
		return this.getHandle().getMaxSpeed();
	}

	@Override
	public boolean isSlowWhenEmpty() {
		return this.getHandle().isSlowWhenEmpty();
	}

	@Override
	public void setDamage(double damage) {
		this.getHandle().setDamage(damage);
	}

	@Override
	public void setDerailedVelocityMod(Vector derailed) {
		this.getHandle().setDerailedVelocityMod(CraftConverter.toBukkitVector(derailed));
	}

	@Override
	public void setDisplayBlock(MaterialData data) {
		this.getHandle().setDisplayBlock(CraftConverter.toBukkitData(data));
	}

	@Override
	public void setDisplayBlockOffset(int offset) {
		this.getHandle().setDisplayBlockOffset(offset);
	}

	@Override
	public void setFlyingVelocityMod(Vector flying) {
		this.getHandle().setFlyingVelocityMod(CraftConverter.toBukkitVector(flying));
	}

	@Override
	public void setMaxSpeed(double speed) {
		this.getHandle().setMaxSpeed(speed);
	}

	@Override
	public void setSlowWhenEmpty(boolean value) {
		this.getHandle().setSlowWhenEmpty(value);
	}

}