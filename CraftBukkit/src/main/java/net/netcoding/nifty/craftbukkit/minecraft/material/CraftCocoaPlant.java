package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.CocoaPlant;

public final class CraftCocoaPlant extends CraftMaterialData implements CocoaPlant {

	public CraftCocoaPlant(org.bukkit.material.CocoaPlant cocoaPlant) {
		super(cocoaPlant);
	}

	@Override
	public CocoaPlant clone() {
		return (CocoaPlant)super.clone();
	}

}