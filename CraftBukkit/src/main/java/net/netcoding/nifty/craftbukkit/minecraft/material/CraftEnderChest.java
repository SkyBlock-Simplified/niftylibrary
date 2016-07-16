package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.EnderChest;

public final class CraftEnderChest extends CraftMaterialData implements EnderChest {

	public CraftEnderChest(org.bukkit.material.EnderChest enderChest) {
		super(enderChest);
	}

	@Override
	public EnderChest clone() {
		return (EnderChest)super.clone();
	}

}