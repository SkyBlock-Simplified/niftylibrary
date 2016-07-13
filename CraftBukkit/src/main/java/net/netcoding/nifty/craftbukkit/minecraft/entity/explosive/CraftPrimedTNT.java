package net.netcoding.nifty.craftbukkit.minecraft.entity.explosive;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.explosive.PrimedTNT;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftPrimedTNT extends CraftExplosive implements PrimedTNT {

	public CraftPrimedTNT(org.bukkit.entity.TNTPrimed tntPrimed) {
		super(tntPrimed);
	}

	@Override
	public org.bukkit.entity.TNTPrimed getHandle() {
		return (org.bukkit.entity.TNTPrimed)super.getHandle();
	}

	@Override
	public int getFuseTicks() {
		return this.getHandle().getFuseTicks();
	}

	@Override
	public Entity getSource() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getSource());
	}

	@Override
	public void setFuseTicks(int fuseTicks) {
		this.getHandle().setFuseTicks(fuseTicks);
	}

}