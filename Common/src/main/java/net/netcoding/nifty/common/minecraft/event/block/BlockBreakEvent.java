package net.netcoding.nifty.common.minecraft.event.block;

import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.player.PlayerEvent;

public interface BlockBreakEvent extends BlockExpEvent, Cancellable, PlayerEvent {

}