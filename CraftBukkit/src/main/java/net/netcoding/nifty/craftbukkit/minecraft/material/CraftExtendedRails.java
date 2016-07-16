package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.ExtendedRails;

public class CraftExtendedRails extends CraftRails implements ExtendedRails {

	public CraftExtendedRails(org.bukkit.material.ExtendedRails extendedRails) {
		super(extendedRails);
	}

	@Override
	public ExtendedRails clone() {
		return (ExtendedRails)super.clone();
	}

}