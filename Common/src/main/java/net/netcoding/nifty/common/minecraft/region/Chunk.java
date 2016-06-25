package net.netcoding.nifty.common.minecraft.region;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.BlockState;
import net.netcoding.nifty.common.minecraft.entity.Entity;

@SuppressWarnings("deprecation")
public interface Chunk {

	Block getBlock(int var1, int var2, int var3);

	Entity[] getEntities();

	BlockState[] getTileEntities();

	World getWorld();

	int getX();

	int getZ();

	boolean isLoaded();

	default boolean load() {
		return this.load(true);
	}

	boolean load(boolean generate);

	default boolean unload() {
		return this.unload(true);
	}

	default boolean unload(boolean save) {
		return this.unload(save, true);
	}

	@Deprecated
	boolean unload(boolean save, boolean safe);

}