package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.entity.Item;
import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface PlayerDropItemEvent extends Cancellable, PlayerEvent {

	Item getItem();

}