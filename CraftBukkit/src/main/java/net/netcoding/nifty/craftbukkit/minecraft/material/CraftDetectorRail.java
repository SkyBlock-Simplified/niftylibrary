package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.DetectorRail;

public final class CraftDetectorRail extends CraftExtendedRails implements DetectorRail {

	public CraftDetectorRail(org.bukkit.material.DetectorRail detectorRail) {
		super(detectorRail);
	}

	@Override
	public DetectorRail clone() {
		return (DetectorRail)super.clone();
	}

}