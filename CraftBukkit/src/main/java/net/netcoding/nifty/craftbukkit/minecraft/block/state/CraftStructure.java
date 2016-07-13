package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Structure;

public final class CraftStructure extends CraftBlockState implements Structure {

	public CraftStructure(org.bukkit.block.Structure structure) {
		super(structure);
	}

	@Override
	public org.bukkit.block.Structure getHandle() {
		return (org.bukkit.block.Structure)super.getHandle();
	}

}