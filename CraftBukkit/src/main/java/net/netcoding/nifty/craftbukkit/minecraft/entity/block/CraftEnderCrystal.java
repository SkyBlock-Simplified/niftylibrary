package net.netcoding.nifty.craftbukkit.minecraft.entity.block;

import net.netcoding.nifty.common.minecraft.entity.block.EnderCrystal;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

public final class CraftEnderCrystal extends CraftEntity implements EnderCrystal {

	public CraftEnderCrystal(org.bukkit.entity.EnderCrystal enderCrystal) {
		super(enderCrystal);
	}

	@Override
	public Location getBeamTarget() {
		return new CraftLocation(this.getHandle().getBeamTarget());
	}

	@Override
	public org.bukkit.entity.EnderCrystal getHandle() {
		return (org.bukkit.entity.EnderCrystal)super.getHandle();
	}

	@Override
	public boolean isShowingBottom() {
		return this.getHandle().isShowingBottom();
	}

	@Override
	public void setBeamTarget(Location location) {
		this.getHandle().setBeamTarget(((CraftLocation)location).getHandle());
	}

	@Override
	public void setShowingBottom(boolean showing) {
		this.getHandle().setShowingBottom(showing);
	}

}