package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.NetherWarts;

public final class CraftNetherWarts extends CraftMaterialData implements NetherWarts {

	public CraftNetherWarts(org.bukkit.material.NetherWarts netherWarts) {
		super(netherWarts);
	}

	@Override
	public NetherWarts clone() {
		return (NetherWarts)super.clone();
	}

}