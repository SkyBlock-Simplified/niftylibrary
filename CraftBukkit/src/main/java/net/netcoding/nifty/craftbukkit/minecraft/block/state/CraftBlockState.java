package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.block.state.BlockState;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlockType;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

@SuppressWarnings("deprecation")
public class CraftBlockState implements BlockState {

	private final org.bukkit.block.BlockState state;
	private final Location location;
	private final Block block;

	public CraftBlockState(org.bukkit.block.BlockState state) {
		this.state = state;
		this.location = new CraftLocation(state.getLocation());
		this.block = new CraftBlock(state.getBlock());
	}

	public static BlockState convertBukkitBlock(org.bukkit.block.Block bukkitBlock) {
		return convertBukkitState(bukkitBlock.getState());
	}

	public static BlockState convertBukkitState(org.bukkit.block.BlockState bukkitState) {
		CraftBlockType type = CraftBlockType.getByState(bukkitState.getClass());
		return type.getBlockClass().cast(new Reflection(type.getBlockClass()).newInstance(bukkitState));
	}

	@Override
	public Block getBlock() {
		return this.block;
	}

	@Override
	public MaterialData getData() {
		return CraftConverter.fromBukkitData(this.getHandle().getData());
	}

	public org.bukkit.block.BlockState getHandle() {
		return this.state;
	}

	@Override
	public byte getLightLevel() {
		return this.getHandle().getLightLevel();
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public byte getRawData() {
		return this.getHandle().getRawData();
	}

	@Override
	public Material getType() {
		return Material.valueOf(this.getHandle().getType().name());
	}

	@Override
	public boolean isPlaced() {
		return this.getHandle().isPlaced();
	}

	@Override
	public void setData(MaterialData data) {
		this.getHandle().setData(CraftConverter.toBukkitData(data));
	}

	@Override
	public boolean setTypeId(int type) {
		return this.getHandle().setTypeId(type);
	}

	@Override
	public void setRawData(byte data) {
		this.getHandle().setRawData(data);
	}

	@Override
	public boolean update(boolean force, boolean applyPhysics) {
		return this.getHandle().update(force, applyPhysics);
	}

}