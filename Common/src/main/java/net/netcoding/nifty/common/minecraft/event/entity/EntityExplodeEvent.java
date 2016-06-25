package net.netcoding.nifty.common.minecraft.event.entity;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.region.Location;

import java.util.List;

public interface EntityExplodeEvent extends Cancellable, EntityEvent {

	List<Block> getBlocks();

	Location getLocation();

	float getYield();

	void setYield(float value);

}