package net.netcoding.nifty.common.minecraft.event.hanging;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;

public interface HangingPlaceEvent extends HangingEvent, PlayerEvent, Cancellable {

	Block getBlock();

	BlockFace getBlockFace();

}