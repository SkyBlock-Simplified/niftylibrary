package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.RedstoneWire;

public final class CraftRedstoneWire extends CraftMaterialData implements RedstoneWire {

	public CraftRedstoneWire(org.bukkit.material.RedstoneWire redstoneWire) {
		super(redstoneWire);
	}

	@Override
	public RedstoneWire clone() {
		return (RedstoneWire)super.clone();
	}

}