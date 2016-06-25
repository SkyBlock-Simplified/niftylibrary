package net.netcoding.nifty.craftbukkit.api.nbt;

import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.reflection.CraftMinecraftPackage;
import net.netcoding.nifty.common._new_.api.nbt.NbtBlockCompound;
import net.netcoding.nifty.common._new_.api.nbt.NbtCompound;
import net.netcoding.nifty.common._new_.api.nbt.NbtFactory;
import net.netcoding.nifty.common._new_.api.nbt.NbtItemCompound;
import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.reflection.BukkitReflection;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;

public final class CraftNbtFactory extends NbtFactory<CraftItemStack, Block> {

	private static CraftNbtFactory INSTANCE;
	public static final Reflection CRAFT_ITEM_STACK = new BukkitReflection("CraftItemStack", "inventory", CraftMinecraftPackage.CRAFTBUKKIT);
	public static final Reflection CRAFT_WORLD = new BukkitReflection("CraftWorld", CraftMinecraftPackage.CRAFTBUKKIT);
	public static final Reflection CRAFT_BLOCK = new BukkitReflection("CraftBlock", "block", CraftMinecraftPackage.CRAFTBUKKIT);

	protected CraftNbtFactory() { }

	@Override
	protected void checkItem(CraftItemStack item) {
		if (item == null)
			throw new IllegalArgumentException("Stack cannot be NULL!");

		if (getCraftItemStack(item.getBukkitItem()).getType() == org.bukkit.Material.AIR)
			throw new UnsupportedOperationException(StringUtil.format("ItemStack type {0} cannot store NMS information!", item.getType()));
	}

	@Override
	protected void checkBlock(Block block) {
		if (block == null)
			throw new IllegalArgumentException("Block cannot be NULL!");

		if (!CRAFT_BLOCK.getClazz().isAssignableFrom(block.getClass())) // TODO
			throw new UnsupportedOperationException("Block must be a CraftBlock!");
	}

	@Override
	protected void checkEntity(Entity entity) {
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be NULL!");

		// TODO: Other Checks
	}

	@Override
	protected NbtBlockCompound fromBlockTag0(Block block) {
		return new CraftNbtBlockCompound(block, createRootNativeCompound());
	}

	@Override
	protected NbtItemCompound fromItemTag0(CraftItemStack item) {
		Object nmsItem = CRAFT_ITEM_STACK.invokeMethod(NMS_ITEM_STACK.getClazz(), null, item.getBukkitItem());
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
		if (INSTANCE == null)
			INSTANCE = new CraftNbtFactory();

		return INSTANCE;
	}

	@Override
	protected void setBlockTag0(Block block, NbtCompound compound) {
		if (!(compound instanceof NbtBlockCompound)) {
			if ((boolean) NMS_BLOCK.invokeMethod("isTileEntity", CRAFT_BLOCK.invokeMethod(NMS_BLOCK.getClazz(), block))) {
				compound.put("x", block.getX());
				compound.put("y", block.getY());
				compound.put("z", block.getZ());
				Object craftWorld = CRAFT_WORLD.getClazz().cast(block.getWorld());
				Object tileEntity = CRAFT_WORLD.invokeMethod(NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
				NMS_TILE_ENTITY.invokeMethod("a", tileEntity, compound.getHandle());
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