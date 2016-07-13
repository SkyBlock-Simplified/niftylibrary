package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.Fireball;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.CraftProjectile;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public class CraftFireball extends CraftProjectile implements Fireball {

	public CraftFireball(org.bukkit.entity.Fireball fireball) {
		super(fireball);
	}

	@Override
	public Vector getDirection() {
		return CraftConverter.fromBukkitVector(this.getHandle().getDirection());
	}

	@Override
	public org.bukkit.entity.Fireball getHandle() {
		return (org.bukkit.entity.Fireball)super.getHandle();
	}

	@Override
	public float getYield() {
		return this.getHandle().getYield();
	}

	@Override
	public boolean isIncendiary() {
		return this.getHandle().isIncendiary();
	}

	@Override
	public void setDirection(Vector direction) {
		this.getHandle().setDirection(CraftConverter.toBukkitVector(direction));
	}

	@Override
	public void setIncendiary(boolean value) {
		this.getHandle().setIsIncendiary(value);
	}

	@Override
	public void setYield(float yield) {
		this.getHandle().setYield(yield);
	}

}