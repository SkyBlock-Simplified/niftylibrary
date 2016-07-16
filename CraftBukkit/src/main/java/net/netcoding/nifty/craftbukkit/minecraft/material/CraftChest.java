package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Chest;

public final class CraftChest extends CraftMaterialData implements Chest {

	public CraftChest(org.bukkit.material.Chest chest) {
		super(chest);
	}

	@Override
	public Chest clone() {
		return (Chest)super.clone();
	}

}