package net.netcoding.nifty.common.minecraft.event.player;

import net.netcoding.nifty.common.minecraft.region.Location;

public interface PlayerRespawnEvent extends PlayerEvent {

	Location getRespawnLocation();

	boolean isBedSpawn();

	void setRespawnLocation(Location location);

}