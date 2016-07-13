package net.netcoding.nifty.craftbukkit.minecraft.entity.explosive;

import net.netcoding.nifty.common.minecraft.entity.explosive.Explosive;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public abstract class CraftExplosive extends CraftEntity implements Explosive {

	protected CraftExplosive(org.bukkit.entity.Explosive explosive) {
		super(explosive);
	}

	@Override
	public org.bukkit.entity.Explosive getHandle() {
		return (org.bukkit.entity.Explosive)super.getHandle();
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
	public void setIncendiary(boolean value) {
		this.getHandle().setIsIncendiary(value);
	}

	@Override
	public void setYield(float yield) {
		this.getHandle().setYield(yield);
	}

}