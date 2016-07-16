package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Diode;

public final class CraftDiode extends CraftMaterialData implements Diode {

	public CraftDiode(org.bukkit.material.Diode diode) {
		super(diode);
	}

	@Override
	public Diode clone() {
		return (Diode)super.clone();
	}

}