package net.netcoding.nifty.common._new_.minecraft.event.block.piston;

import net.netcoding.nifty.common._new_.minecraft.block.Block;

import java.util.List;

public interface BlockPistonExtendEvent extends BlockPistonEvent {

	List<Block> getBlocks();

	int getLength();

}