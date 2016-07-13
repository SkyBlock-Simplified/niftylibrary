package net.netcoding.nifty.craftbukkit.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.entity.hanging.Hanging;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public abstract class CraftHanging extends CraftEntity implements Hanging {

	protected CraftHanging(org.bukkit.entity.Hanging hanging) {
		super(hanging);
	}

	@Override
	public BlockFace getAttachedFace() {
		return BlockFace.valueOf(this.getHandle().getAttachedFace().name());
	}

	@Override
	public BlockFace getFacing() {
		return BlockFace.valueOf(this.getHandle().getFacing().name());
	}

	@Override
	public org.bukkit.entity.Hanging getHandle() {
		return (org.bukkit.entity.Hanging)super.getHandle();
	}

	@Override
	public boolean setFacingDirection(BlockFace face, boolean force) {
		return this.getHandle().setFacingDirection(org.bukkit.block.BlockFace.valueOf(face.name()), force);
	}

}