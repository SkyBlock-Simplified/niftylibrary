package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Furnace;

public final class CraftFurnace extends CraftMaterialData implements Furnace {

	public CraftFurnace(org.bukkit.material.Furnace furnace) {
		super(furnace);
	}

	@Override
	public Furnace clone() {
		return (Furnace)super.clone();
	}

}