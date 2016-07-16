package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Cake;

public final class CraftCake extends CraftMaterialData implements Cake {

	public CraftCake(org.bukkit.material.Cake cake) {
		super(cake);
	}

	@Override
	public Cake clone() {
		return (Cake)super.clone();
	}

}