package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.region.Location;

public interface PlayerMoveEvent extends Cancellable, PlayerEvent {

	Location getFrom();

	Location getTo();

	void setFrom(Location location);

	void setTo(Location location);

}