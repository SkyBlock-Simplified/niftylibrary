package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Torch;

public class CraftTorch extends CraftMaterialData implements Torch {

	public CraftTorch(org.bukkit.material.Torch torch) {
		super(torch);
	}

	@Override
	public Torch clone() {
		return (Torch)super.clone();
	}

}