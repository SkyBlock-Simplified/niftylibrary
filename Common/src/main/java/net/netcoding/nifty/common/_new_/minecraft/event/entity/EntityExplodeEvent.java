package net.netcoding.nifty.common._new_.minecraft.event.entity;

import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.region.Location;

import java.util.List;

public interface EntityExplodeEvent extends Cancellable, EntityEvent {

	List<Block> getBlocks();

	Location getLocation();

	float getYield();

	void setYield(float value);

}