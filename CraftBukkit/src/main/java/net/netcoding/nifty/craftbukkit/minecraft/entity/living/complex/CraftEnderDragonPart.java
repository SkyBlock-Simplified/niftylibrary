package net.netcoding.nifty.craftbukkit.minecraft.entity.living.complex;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragon;
import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragonPart;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftEnderDragonPart extends CraftComplexEntityPart implements EnderDragonPart {

	public CraftEnderDragonPart(org.bukkit.entity.EnderDragonPart enderDragonPart) {
		super(enderDragonPart);
	}

	@Override
	public void damage(double amount, Entity source) {
		this.getHandle().damage(amount, ((CraftEntity)source).getHandle());
	}

	@Override
	public org.bukkit.entity.EnderDragonPart getHandle() {
		return (org.bukkit.entity.EnderDragonPart)super.getHandle();
	}

	@Override
	public double getHealth() {
		return this.getHandle().getHealth();
	}

	@Override
	public double getMaxHealth() {
		return this.getHandle().getMaxHealth();
	}

	@Override
	public EnderDragon getParent() {
		return (EnderDragon)super.getParent();
	}

	@Override
	public void resetMaxHealth() {
		this.getHandle().resetMaxHealth();
	}

	@Override
	public void setHealth(double health) {
		this.getHandle().setHealth(health);
	}

	@Override
	public void setMaxHealth(double maxHealth) {
		this.getHandle().setMaxHealth(maxHealth);
	}

}