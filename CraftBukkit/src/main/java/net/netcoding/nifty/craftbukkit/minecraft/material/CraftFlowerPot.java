package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.FlowerPot;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

@SuppressWarnings("deprecation")
public final class CraftFlowerPot extends CraftMaterialData implements FlowerPot {

	public CraftFlowerPot(org.bukkit.material.FlowerPot flowerPot) {
		super(flowerPot);
	}

	@Override
	public FlowerPot clone() {
		return (FlowerPot)super.clone();
	}

	@Override
	public MaterialData getContents() {
		return CraftConverter.fromBukkitData(this.getHandle());
	}

}