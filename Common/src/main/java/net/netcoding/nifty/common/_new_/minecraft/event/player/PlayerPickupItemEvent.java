package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.entity.Item;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

public interface PlayerPickupItemEvent extends Cancellable, PlayerEvent {

	Item getItem();

	int getRemaining();

}