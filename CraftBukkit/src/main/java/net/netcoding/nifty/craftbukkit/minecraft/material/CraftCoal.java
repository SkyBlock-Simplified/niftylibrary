package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Coal;

public final class CraftCoal extends CraftMaterialData implements Coal {

	public CraftCoal(org.bukkit.material.Coal coal) {
		super(coal);
	}

	@Override
	public Coal clone() {
		return (Coal)super.clone();
	}

}