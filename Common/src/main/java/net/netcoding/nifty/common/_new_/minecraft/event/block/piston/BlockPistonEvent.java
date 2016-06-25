package net.netcoding.nifty.common._new_.minecraft.event.block.piston;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.block.BlockEvent;
import net.netcoding.nifty.common._new_.minecraft.material.Material;

public interface BlockPistonEvent extends BlockEvent, Cancellable {

	BlockFace getDirection();

	default boolean isSticky() {
		return Material.PISTON_STICKY_BASE == this.getBlock().getType() || Material.PISTON_MOVING_PIECE == this.getBlock().getType();
	}

}