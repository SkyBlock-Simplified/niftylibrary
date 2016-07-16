package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.PoweredRail;

public final class CraftPoweredRail extends CraftExtendedRails implements PoweredRail {

	public CraftPoweredRail(org.bukkit.material.PoweredRail poweredRail) {
		super(poweredRail);
	}

	@Override
	public PoweredRail clone() {
		return (PoweredRail)super.clone();
	}

}