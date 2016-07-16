package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Tripwire;

public final class CraftTripwire extends CraftMaterialData implements Tripwire {

	public CraftTripwire(org.bukkit.material.Tripwire tripwire) {
		super(tripwire);
	}

	@Override
	public Tripwire clone() {
		return (Tripwire)super.clone();
	}

}