package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.common.minecraft.region.Chunk;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;

public interface BlockState { // Metadatable

	Block getBlock();

	MaterialData getData();

	byte getLightLevel();

	Material getType();

	int getTypeId();

	World getWorld();

	Location getLocation();

	Location getLocation(Location location);

	Chunk getChunk();

	boolean isPlaced();

	void setData(MaterialData data);

	void setType(Material type);

	boolean setTypeId(int type);

	@Deprecated
	byte getRawData();

	@Deprecated
	void setRawData(byte data);

	boolean update();

	boolean update(boolean force);

	boolean update(boolean force, boolean applyPhysics);

}