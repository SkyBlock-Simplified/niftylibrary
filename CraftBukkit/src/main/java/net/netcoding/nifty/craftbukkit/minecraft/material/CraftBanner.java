package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Banner;

public final class CraftBanner extends CraftMaterialData implements Banner {

	public CraftBanner(org.bukkit.material.Banner banner) {
		super(banner);
	}

	@Override
	public Banner clone() {
		return (Banner)super.clone();
	}

}