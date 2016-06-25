package net.netcoding.nifty.common.minecraft.event.block.piston;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.region.Location;

import java.util.List;

public interface BlockPistonRetractEvent extends BlockPistonEvent {

	List<Block> getBlocks();

	default Location getRetractLocation() {
		return this.getBlock().getRelative(this.getDirection(), 2).getLocation();
	}

}