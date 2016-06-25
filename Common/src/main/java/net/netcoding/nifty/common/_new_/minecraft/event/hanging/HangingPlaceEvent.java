package net.netcoding.nifty.common._new_.minecraft.event.hanging;

import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerEvent;

public interface HangingPlaceEvent extends HangingEvent, PlayerEvent, Cancellable {

	Block getBlock();

	BlockFace getBlockFace();

}