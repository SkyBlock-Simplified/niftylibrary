package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.common.minecraft.region.Chunk;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;

public interface BlockState { // Metadatable

	Block getBlock();

	default Chunk getChunk() {
		return this.getBlock().getChunk();
	}

	MaterialData getData();

	byte getLightLevel();

	Location getLocation();

	default Location getLocation(Location location) {
		if (location == null)
			return null;

		Location old = this.getLocation();
		location.setPitch(old.getPitch());
		location.setX(old.getX());
		location.setY(old.getY());
		location.setYaw(old.getYaw());
		location.setZ(old.getZ());
		location.setWorld(old.getWorld());
		return old;
	}

	@Deprecated
	byte getRawData();

	Material getType();

	default int getTypeId() {
		return this.getType().getId();
	}

	default World getWorld() {
		return this.getChunk().getWorld();
	}

	boolean isPlaced();

	void setData(MaterialData data);

	default void setType(Material type) {
		this.setTypeId(type.getId());
	}

	boolean setTypeId(int type);

	@Deprecated
	void setRawData(byte data);

	default boolean update() {
		return this.update(false);
	}

	default boolean update(boolean force) {
		return this.update(force, true);
	}

	boolean update(boolean force, boolean applyPhysics);

}