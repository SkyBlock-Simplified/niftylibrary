package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.FlowerPot;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

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
		return CraftConverter.fromBukkitData(this.getHandle().getContents());
	}

	@Override
	public void setContents(MaterialData data) {
		this.getHandle().setContents(CraftConverter.toBukkitData(data));
	}

}