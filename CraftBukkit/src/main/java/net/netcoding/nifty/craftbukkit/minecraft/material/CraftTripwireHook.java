package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.TripwireHook;

public final class CraftTripwireHook extends CraftMaterialData implements TripwireHook {

	public CraftTripwireHook(org.bukkit.material.TripwireHook tripwireHook) {
		super(tripwireHook);
	}

	@Override
	public TripwireHook clone() {
		return (TripwireHook)super.clone();
	}

}