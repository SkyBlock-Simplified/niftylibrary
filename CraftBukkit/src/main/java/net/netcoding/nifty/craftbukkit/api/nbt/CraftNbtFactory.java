package net.netcoding.nifty.craftbukkit.api.nbt;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_9_R2.TileEntity;
import net.netcoding.nifty.common.api.nbt.*;
import net.netcoding.nifty.common.reflection.MinecraftReflection;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.craftbukkit.minecraft.block.CraftBlock;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.reflection.CraftMinecraftPackage;

public final class CraftNbtFactory extends NbtFactory<CraftItemStack, CraftBlock, CraftEntity> {

	private static final CraftNbtFactory INSTANCE = new CraftNbtFactory();
	public static final Reflection CRAFT_ITEM_STACK = new MinecraftReflection("CraftItemStack", "inventory", CraftMinecraftPackage.CRAFTBUKKIT);
	public static final Reflection CRAFT_WORLD = new MinecraftReflection("CraftWorld", CraftMinecraftPackage.CRAFTBUKKIT);
	public static final Reflection CRAFT_BLOCK = new MinecraftReflection("CraftBlock", "block", CraftMinecraftPackage.CRAFTBUKKIT);

	private CraftNbtFactory() { }

	@Override
	protected void checkItem(CraftItemStack item) {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");

		if (getCraftItemStack(item.getHandle()).getType() == org.bukkit.Material.AIR)
			throw new UnsupportedOperationException(StringUtil.format("ItemStack type ''{0}'' cannot store NMS information!", item.getType()));
	}

	@Override
	protected void checkBlock(CraftBlock block) {
		Preconditions.checkArgument(block != null, "Block cannot be NULL!");

		if (!CRAFT_BLOCK.getClazz().isAssignableFrom(block.getHandle().getClass()))
			throw new UnsupportedOperationException("Block must be a CraftBlock!");
	}

	@Override
	protected void checkEntity(CraftEntity entity) {
		Preconditions.checkArgument(entity != null, "Entity cannot be NULL!");
	}

	@Override
	protected NbtBlockCompound<CraftBlock> fromBlockTag0(CraftBlock block) {
		return new CraftNbtBlockCompound(block, createRootNativeCompound());
	}

	@Override
	protected NbtEntityCompound<CraftEntity> fromEntityTag0(CraftEntity entity) {
		throw new UnsupportedOperationException("Entity NBT is not currently supported!"); // TODO
	}

	@Override
	protected NbtItemCompound<CraftItemStack> fromItemTag0(CraftItemStack item) {
		Object nmsItem = CRAFT_ITEM_STACK.invokeMethod(NMS_ITEM_STACK.getClazz(), null, item.getHandle());
		Object handle = NMS_ITEM_STACK.invokeMethod(NBT_TAG_COMPOUND.getClazz(), nmsItem);

		if (handle == null)
			handle = createRootNativeCompound();

		return new CraftNbtItemCompound(item, nmsItem, handle);
	}

	/**
	 * Retrieve a CraftItemStack version of the stack.
	 *
	 * @param stack - the stack to convert.
	 * @return The CraftItemStack version.
	 */
	public static org.bukkit.inventory.ItemStack getCraftItemStack(org.bukkit.inventory.ItemStack stack) {
		return (stack == null || CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass())) ? stack : (org.bukkit.inventory.ItemStack)CRAFT_ITEM_STACK.invokeMethod("asCraftCopy", null, stack);
	}

	public static CraftNbtFactory getInstance() {
		return INSTANCE;
	}

	@Override
	protected void setBlockTag0(CraftBlock block, NbtCompound compound) {
		if (!(compound instanceof NbtBlockCompound)) {
			org.bukkit.block.Block bukkitBlock = block.getHandle();

			if ((boolean) NMS_BLOCK.invokeMethod("isTileEntity", CRAFT_BLOCK.invokeMethod(NMS_BLOCK.getClazz(), bukkitBlock))) {
				compound.put("x", bukkitBlock.getX());
				compound.put("y", bukkitBlock.getY());
				compound.put("z", bukkitBlock.getZ());
				TileEntity te;
				Object craftWorld = CRAFT_WORLD.getClazz().cast(bukkitBlock.getWorld());
				Object tileEntity = CRAFT_WORLD.invokeMethod(NMS_TILE_ENTITY.getClazz(), craftWorld, bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
				NMS_TILE_ENTITY.invokeMethod(Void.class, tileEntity, compound.getHandle());
			}
		}
	}

	@Override
	protected void setItemTag0(CraftItemStack item, NbtCompound compound) {
		if (!(compound instanceof NbtItemCompound)) {
			Object nmsItem = CRAFT_ITEM_STACK.getValue(NMS_ITEM_STACK.getClazz(), item);
			NMS_ITEM_STACK.setValue(NBT_TAG_COMPOUND.getClazz(), nmsItem, compound.getHandle());
		}
	}

}