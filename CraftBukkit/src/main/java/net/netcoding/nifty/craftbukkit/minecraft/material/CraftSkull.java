package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Skull;

public final class CraftSkull extends CraftMaterialData implements Skull {

	public CraftSkull(org.bukkit.material.Skull skull) {
		super(skull);
	}

	@Override
	public Skull clone() {
		return (Skull)super.clone();
	}

}