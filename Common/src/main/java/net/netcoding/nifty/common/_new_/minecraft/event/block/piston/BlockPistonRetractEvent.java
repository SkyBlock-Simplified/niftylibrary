package net.netcoding.nifty.common._new_.minecraft.event.block.piston;

import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.region.Location;

import java.util.List;

public interface BlockPistonRetractEvent extends BlockPistonEvent {

	List<Block> getBlocks();

	default Location getRetractLocation() {
		return this.getBlock().getRelative(this.getDirection(), 2).getLocation();
	}

}