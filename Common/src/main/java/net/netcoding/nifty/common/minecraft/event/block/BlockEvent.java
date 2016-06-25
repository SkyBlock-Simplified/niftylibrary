package net.netcoding.nifty.common.minecraft.event.block;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.event.Event;

public interface BlockEvent extends Event {

	Block getBlock();

}