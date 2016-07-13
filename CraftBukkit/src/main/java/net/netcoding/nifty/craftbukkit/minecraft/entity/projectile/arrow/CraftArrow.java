package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow;

import net.netcoding.nifty.common.minecraft.entity.projectile.arrow.Arrow;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.CraftProjectile;

public class CraftArrow extends CraftProjectile implements Arrow {

	public CraftArrow(org.bukkit.entity.Arrow arrow) {
		super(arrow);
	}

	@Override
	public org.bukkit.entity.Arrow getHandle() {
		return (org.bukkit.entity.Arrow)super.getHandle();
	}

	@Override
	public int getKnockbackStrength() {
		return this.getHandle().getKnockbackStrength();
	}

	@Override
	public boolean isCritical() {
		return this.getHandle().isCritical();
	}

	@Override
	public void setCritical(boolean value) {
		this.getHandle().setCritical(value);
	}

	@Override
	public void setKnockbackStrength(int knockbackStrength) {
		this.getHandle().setKnockbackStrength(knockbackStrength);
	}

}