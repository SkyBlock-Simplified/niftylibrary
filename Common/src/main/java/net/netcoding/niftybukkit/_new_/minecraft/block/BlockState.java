package net.netcoding.niftybukkit._new_.minecraft.block;

import net.netcoding.niftybukkit._new_.minecraft.region.Chunk;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;
import net.netcoding.niftybukkit._new_.minecraft.region.World;
import net.netcoding.niftybukkit._new_.minecraft.material.Material;
import net.netcoding.niftybukkit._new_.minecraft.material.MaterialData;
import org.bukkit.block.Block;

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