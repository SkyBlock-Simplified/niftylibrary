package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Comparator;

public final class CraftComparator extends CraftMaterialData implements Comparator {

	public CraftComparator(org.bukkit.material.Comparator comparator) {
		super(comparator);
	}

	@Override
	public Comparator clone() {
		return (Comparator)super.clone();
	}

}