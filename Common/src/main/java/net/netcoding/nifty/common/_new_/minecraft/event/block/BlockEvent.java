package net.netcoding.nifty.common._new_.minecraft.event.block;

import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.event.Event;

public interface BlockEvent extends Event {

	Block getBlock();

}