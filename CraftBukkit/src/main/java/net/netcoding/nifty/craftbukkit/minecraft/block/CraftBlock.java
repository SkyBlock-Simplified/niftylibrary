package net.netcoding.nifty.craftbukkit.minecraft.block;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.block.PistonMoveReaction;
import net.netcoding.nifty.common.minecraft.block.state.BlockState;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.region.Biome;
import net.netcoding.nifty.common.minecraft.region.Chunk;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.craftbukkit.minecraft.block.state.CraftBlockState;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftChunk;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public final class CraftBlock implements Block {

	private final org.bukkit.block.Block block;
	private final Chunk chunk;
	private final Location location;

	public CraftBlock(org.bukkit.block.Block block) {
		this.block = block;
		this.chunk = new CraftChunk(block.getChunk());
		this.location = new CraftLocation(block.getLocation());
	}

	@Override
	public boolean breakNaturally() {
		return this.getHandle().breakNaturally();
	}

	@Override
	public boolean breakNaturally(ItemStack tool) {
		return this.getHandle().breakNaturally(((CraftItemStack)tool).getHandle());
	}

	@Override
	public Biome getBiome() {
		return Biome.valueOf(this.getHandle().getBiome().name());
	}

	@Override
	public int getBlockPower(BlockFace face) {
		return this.getHandle().getBlockPower(org.bukkit.block.BlockFace.valueOf(face.name()));
	}

	@Override
	public Chunk getChunk() {
		return this.chunk;
	}

	@Override
	public byte getData() {
		return this.getHandle().getData();
	}

	@Override
	public Collection<ItemStack> getDrops() {
		return this.getHandle().getDrops().stream().map(CraftItemStack::new).collect(Collectors.toSet());
	}

	@Override
	public Collection<ItemStack> getDrops(ItemStack tool) {
		return this.getHandle().getDrops(((CraftItemStack)tool).getHandle()).stream().map(CraftItemStack::new).collect(Collectors.toSet());
	}

	@Override
	public BlockFace getFace(Block block) {
		return BlockFace.valueOf(this.getHandle().getFace(((CraftBlock)block).getHandle()).name());
	}

	public org.bukkit.block.Block getHandle() {
		return this.block;
	}

	@Override
	public double getHumidity() {
		return this.getHandle().getHumidity();
	}

	@Override
	public byte getLightLevel() {
		return this.getHandle().getLightLevel();
	}

	@Override
	public byte getLightFromBlocks() {
		return this.getHandle().getLightFromBlocks();
	}

	@Override
	public byte getLightFromSky() {
		return this.getHandle().getLightFromBlocks();
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return PistonMoveReaction.valueOf(this.getHandle().getPistonMoveReaction().name());
	}

	@Override
	public Block getRelative(int modX, int modY, int modZ) {
		return new CraftBlock(this.getHandle().getRelative(modX, modY, modZ));
	}

	@Override
	public Block getRelative(BlockFace face, int distance) {
		return new CraftBlock(this.getHandle().getRelative(org.bukkit.block.BlockFace.valueOf(face.name()), distance));
	}

	@Override
	public BlockState getState() {
		return CraftBlockState.convertBukkitBlock(this.getHandle());
	}

	@Override
	public double getTemperature() {
		return this.getHandle().getTemperature();
	}

	@Override
	public Material getType() {
		return Material.valueOf(this.getHandle().getType().name());
	}

	@Override
	public int getTypeId() {
		return this.getHandle().getTypeId();
	}

	@Override
	public int getX() {
		return this.getLocation().getBlockX();
	}

	@Override
	public int getY() {
		return this.getLocation().getBlockX();
	}

	@Override
	public int getZ() {
		return this.getLocation().getBlockZ();
	}

	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
		return this.getHandle().isBlockFaceIndirectlyPowered(org.bukkit.block.BlockFace.valueOf(face.name()));
	}

	@Override
	public boolean isBlockFacePowered(BlockFace face) {
		return this.getHandle().isBlockFacePowered(org.bukkit.block.BlockFace.valueOf(face.name()));
	}

	@Override
	public boolean isBlockIndirectlyPowered() {
		return this.getHandle().isBlockIndirectlyPowered();
	}

	@Override
	public boolean isBlockPowered() {
		return this.getHandle().isBlockPowered();
	}

	@Override
	public boolean isEmpty() {
		return this.getHandle().isEmpty();
	}

	@Override
	public boolean isLiquid() {
		return this.getHandle().isLiquid();
	}

	@Override
	public void setBiome(Biome biome) {
		this.getHandle().setBiome(org.bukkit.block.Biome.valueOf(biome.name()));
	}

	@Override
	public void setData(byte data, boolean applyPhysics) {
		this.getHandle().setData(data, applyPhysics);
	}

	@Override
	public boolean setTypeId(int type, boolean applyPhysics) {
		return this.getHandle().setTypeId(type, applyPhysics);
	}

	@Override
	public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
		return this.getHandle().setTypeIdAndData(type, data, applyPhysics);
	}

}