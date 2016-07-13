package net.netcoding.nifty.craftbukkit.minecraft.region;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.state.BlockState;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.region.Chunk;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.block.state.CraftBlockState;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

import java.util.Arrays;
import java.util.Set;

public final class CraftChunk implements Chunk {

	private final org.bukkit.Chunk chunk;
	private final World world;

	public CraftChunk(org.bukkit.Chunk chunk) {
		this.chunk = chunk;
		this.world = new CraftWorld(chunk.getWorld());
	}

	@Override
	public Block getBlock(int var1, int var2, int var3) {
		return null;
	}

	@Override
	public Set<Entity> getEntities() {
		return Arrays.stream(this.getHandle().getEntities()).map(CraftEntity::convertBukkitEntity).collect(Concurrent.toSet());
	}

	public org.bukkit.Chunk getHandle() {
		return this.chunk;
	}

	@Override
	public Set<BlockState> getTileEntities() {
		return Arrays.stream(this.getHandle().getTileEntities()).map(CraftBlockState::convertBukkitState).collect(Concurrent.toSet());
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public int getX() {
		return this.getHandle().getX();
	}

	@Override
	public int getZ() {
		return this.getHandle().getZ();
	}

	@Override
	public boolean isLoaded() {
		return this.getHandle().isLoaded();
	}

	@Override
	public boolean load(boolean generate) {
		return this.getHandle().load(generate);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean unload(boolean save, boolean safe) {
		return this.getHandle().unload(save, safe);
	}

}