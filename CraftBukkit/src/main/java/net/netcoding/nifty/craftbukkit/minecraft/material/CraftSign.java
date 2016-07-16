package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Sign;

public final class CraftSign extends CraftMaterialData implements Sign {

	public CraftSign(org.bukkit.material.Sign sign) {
		super(sign);
	}

	@Override
	public Sign clone() {
		return (Sign)super.clone();
	}

}