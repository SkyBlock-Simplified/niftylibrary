package net.netcoding.niftybukkit._new_.minecraft.region;

import net.netcoding.niftybukkit._new_.minecraft.block.BlockState;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface Chunk {

	Block getBlock(int var1, int var2, int var3);

	Entity[] getEntities();

	BlockState[] getTileEntities();

	World getWorld();

	int getX();

	int getZ();

	boolean isLoaded();

	default boolean load() {
		return this.load(false); // TODO
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