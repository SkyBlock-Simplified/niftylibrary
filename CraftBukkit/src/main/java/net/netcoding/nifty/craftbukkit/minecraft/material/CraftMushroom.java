package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Mushroom;

public final class CraftMushroom extends CraftMaterialData implements Mushroom {

	public CraftMushroom(org.bukkit.material.Mushroom mushroom) {
		super(mushroom);
	}

	@Override
	public Mushroom clone() {
		return (Mushroom)super.clone();
	}

}