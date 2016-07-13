package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.EndGateway;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

public final class CraftEndGateway extends CraftBlockState implements EndGateway {

	public CraftEndGateway(org.bukkit.block.EndGateway endGateway) {
		super(endGateway);
	}

	@Override
	public Location getExitLocation() {
		return new CraftLocation(this.getHandle().getExitLocation());
	}

	@Override
	public org.bukkit.block.EndGateway getHandle() {
		return (org.bukkit.block.EndGateway)super.getHandle();
	}

	@Override
	public boolean isExactTeleport() {
		return this.getHandle().isExactTeleport();
	}

	@Override
	public void setExactTeleport(boolean exact) {
		this.getHandle().setExactTeleport(exact);
	}

	@Override
	public void setExitLocation(Location location) {
		this.getHandle().setExitLocation(((CraftLocation)location).getHandle());
	}

}