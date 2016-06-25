package net.netcoding.nifty.common._new_.minecraft.event.player;

import net.netcoding.nifty.common._new_.minecraft.region.Location;

public interface PlayerRespawnEvent extends PlayerEvent {

	Location getRespawnLocation();

	boolean isBedSpawn();

	void setRespawnLocation(Location location);

}