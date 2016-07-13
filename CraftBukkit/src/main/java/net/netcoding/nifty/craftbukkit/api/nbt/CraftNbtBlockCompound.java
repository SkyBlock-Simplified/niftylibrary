package net.netcoding.nifty.craftbukkit.api.nbt;

import net.netcoding.nifty.common.api.nbt.NbtBlockCompound;
import net.netcoding.nifty.common.api.nbt.NbtFactory;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;

public final class CraftNbtBlockCompound extends NbtBlockCompound<CraftBlock> {

	private boolean isTileEntity;

	CraftNbtBlockCompound(CraftBlock block, Object handle) {
		super(block, handle);
		this.isTileEntity = (boolean)CraftNbtFactory.NMS_BLOCK.invokeMethod("isTileEntity", CraftNbtFactory.CRAFT_BLOCK.invokeMethod(NbtFactory.NMS_BLOCK.getClazz(), this.getWrapped()));
		this.load();
	}

	@Override
	public boolean isTileEntity() {
		return this.isTileEntity;
	}

	@Override
	protected void load() {
		if (this.isTileEntity()) {
			org.bukkit.block.Block bukkitBlock = this.getWrapped().getHandle();
			Object craftWorld = CraftNbtFactory.CRAFT_WORLD.getClazz().cast(bukkitBlock.getWorld());
			Object tileEntity = CraftNbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
			NbtFactory.NMS_TILE_ENTITY.invokeMethod(NbtFactory.NBT_TAG_COMPOUND.getClazz(), tileEntity, this.getHandle());
		}
	}

	@Override
	protected void save() {
		if (this.isTileEntity()) {
			org.bukkit.block.Block bukkitBlock = this.getWrapped().getHandle();
			this.put("x", bukkitBlock.getX());
			this.put("y", bukkitBlock.getY());
			this.put("z", bukkitBlock.getZ());
			Object craftWorld = CraftNbtFactory.CRAFT_WORLD.getClazz().cast(bukkitBlock.getWorld());
			Object tileEntity = CraftNbtFactory.CRAFT_WORLD.invokeMethod(NbtFactory.NMS_TILE_ENTITY.getClazz(), craftWorld, bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
			NbtFactory.NMS_TILE_ENTITY.invokeMethod(Void.class, tileEntity, this.getHandle());
		}
	}

}