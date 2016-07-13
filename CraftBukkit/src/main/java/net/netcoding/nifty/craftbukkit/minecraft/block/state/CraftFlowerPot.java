package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.FlowerPot;
import net.netcoding.nifty.common.minecraft.material.MaterialData;

public final class CraftFlowerPot extends CraftBlockState implements FlowerPot {

	public CraftFlowerPot(org.bukkit.block.FlowerPot flowerPot) {
		super(flowerPot);
	}

	@Override
	public org.bukkit.block.FlowerPot getHandle() {
		return (org.bukkit.block.FlowerPot)super.getHandle();
	}

	@Override
	public MaterialData getContents() {
		return null; // TODO
	}

	@Override
	public void setContents(MaterialData data) {
		// TODO
	}

}