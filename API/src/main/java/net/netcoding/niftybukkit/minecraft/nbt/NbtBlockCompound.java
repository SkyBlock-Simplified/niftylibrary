package net.netcoding.niftybukkit.minecraft.nbt;

import org.bukkit.block.Block;

public final class NbtBlockCompound extends WrappedCompound<Block> {

	private final boolean isTileEntity;

	NbtBlockCompound(Block block, Object handle) {
		super(block, handle);
		this.isTileEntity = (boolean)NbtFactory.NMS_BLOCK.invokeMethod("isTileEntity", NbtFactory.CRAFT_BLOCK.invokeMethod(NbtFactory.NMS_BLOCK.getClazz(), this.getWrapped()));
		this.load();
	}

	public final boolean isTileEntity() {
		return this.isTileEntity;
	}

	@Override
	protected final void load() {
		if (this.isTileEntity()) {
			Block block = this.getWrapped();
			Object craftWorld = NbtFactory.CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = NbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
			NbtFactory.NMS_TILE_ENTITY.invokeMethod("save", tileEntity, this.getHandle());
		}
	}

	@Override
	protected final void save() {
		if (this.isTileEntity()) {
			Block block = this.getWrapped();
			this.put("x", block.getX());
			this.put("y", block.getY());
			this.put("z", block.getZ());
			Object craftWorld = NbtFactory.CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = NbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
			NbtFactory.NMS_TILE_ENTITY.invokeMethod("a", tileEntity, this.getHandle());
		}
	}

}