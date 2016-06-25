package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.region.Location;

public interface PlayerMoveEvent extends Cancellable, PlayerEvent {

	Location getFrom();

	Location getTo();

	void setFrom(Location location);

	void setTo(Location location);

}