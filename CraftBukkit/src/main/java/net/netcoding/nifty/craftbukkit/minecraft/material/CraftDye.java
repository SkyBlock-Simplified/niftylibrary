package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Dye;

public final class CraftDye extends CraftMaterialData implements Dye {

	public CraftDye(org.bukkit.material.Dye dye) {
		super(dye);
	}

	@Override
	public Dye clone() {
		return (Dye)super.clone();
	}

}