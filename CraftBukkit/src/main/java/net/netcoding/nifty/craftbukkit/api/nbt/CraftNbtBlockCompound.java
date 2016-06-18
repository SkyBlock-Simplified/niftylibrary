package net.netcoding.nifty.craftbukkit.api.nbt;

import net.netcoding.niftybukkit._new_.api.nbt.NbtBlockCompound;
import net.netcoding.niftybukkit._new_.api.nbt.NbtFactory;
import net.netcoding.niftybukkit._new_.minecraft.block.Block;

public final class CraftNbtBlockCompound extends NbtBlockCompound<Block> {

	private final boolean isTileEntity;

	CraftNbtBlockCompound(Block block, Object handle) {
		super(block, handle);
		this.isTileEntity = (boolean) CraftNbtFactory.NMS_BLOCK.invokeMethod("isTileEntity", CraftNbtFactory.CRAFT_BLOCK.invokeMethod(NbtFactory.NMS_BLOCK.getClazz(), this.getWrapped()));
		this.load();
	}

	@Override
	public boolean isTileEntity() {
		return this.isTileEntity;
	}

	@Override
	protected final void load() {
		if (this.isTileEntity()) {
			Block block = this.getWrapped();
			Object craftWorld = CraftNbtFactory.CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = CraftNbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
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
			Object craftWorld = CraftNbtFactory.CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = CraftNbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
			NbtFactory.NMS_TILE_ENTITY.invokeMethod("a", tileEntity, this.getHandle());
		}
	}

}