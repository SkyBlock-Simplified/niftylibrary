package net.netcoding.niftybukkit.minecraft.nbt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import com.google.common.primitives.Primitives;
import net.netcoding.niftybukkit.reflection.BukkitReflection;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("deprecation")
public class NbtFactory {

	// https://bukkit.org/threads/library-edit-or-create-nbt-tags-with-a-compact-class-no-obc-nms.178464/
	// https://gist.github.com/aadnk/6753244 (Nov 3, 2013)

	// Convert between NBT id and the equivalent class in java
	static final BiMap<Integer, Class<?>> NBT_CLASS = HashBiMap.create();
	static final BiMap<Integer, NbtType> NBT_ENUM = HashBiMap.create();

	// Reflection
	private static final Reflection CRAFT_ITEM_STACK = new BukkitReflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
	private static final Reflection CRAFT_WORLD = new BukkitReflection("CraftWorld", MinecraftPackage.CRAFTBUKKIT);
	private static final Reflection CRAFT_BLOCK = new BukkitReflection("CraftBlock", "block", MinecraftPackage.CRAFTBUKKIT);
	private static final Reflection NMS_ITEM_STACK = BukkitReflection.getCompatibleForgeReflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER, "item");
	private static final Reflection NMS_TILE_ENTITY = BukkitReflection.getCompatibleForgeReflection("TileEntity", MinecraftPackage.MINECRAFT_SERVER, "tileentity");
	private static final Reflection NMS_BLOCK = new BukkitReflection("Block", MinecraftPackage.MINECRAFT_SERVER);
	private static final Reflection NBT_BASE = BukkitReflection.getCompatibleForgeReflection("NBTBase", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	private static final Reflection NBT_READ_LIMITER = BukkitReflection.getCompatibleForgeReflection("NBTReadLimiter", "nbt", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	static final Reflection NBT_TAG_LIST = BukkitReflection.getCompatibleForgeReflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	private static final Object NBT_READ_NOLIMIT = NBT_READ_LIMITER.getValue(NBT_READ_LIMITER.getClazz(), null);

	/**
	 * Ensure that the given stack can store arbitrary NBT information.
	 *
	 * @param stack - the stack to check.
	 */
	private static void checkItemStack(ItemStack stack) {
		if (stack == null)
			throw new IllegalArgumentException("Stack cannot be NULL!");

		if (!CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass()))
			throw new IllegalArgumentException("Stack must be a CraftItemStack!");

		if (stack.getType() == Material.AIR)
			throw new IllegalArgumentException("ItemStacks representing air cannot store NMS information!");
	}

	private static void checkBlock(Block block) {
		if (!CRAFT_BLOCK.getClazz().isAssignableFrom(block.getClass()))
			throw new IllegalArgumentException("Block must be a CraftBlock!");
	}

	/**
	 * Construct a new NBT list of an unspecified type.
	 *
	 * @return The NBT list.
	 */
	@SafeVarargs
	public static <T> NbtList<T> createList(T... content) {
		return createList(Arrays.asList(content));
	}

	/**
	 * Construct a new NBT list of an unspecified type.
	 *
	 * @return The NBT list.
	 */
	public static <T> NbtList<T> createList(Iterable<T> iterable) {
		NbtList<T> list = new NbtList<>(createNbtTag(NbtType.TAG_LIST, "", null));

		// Add the content as well
		for (T obj : iterable)
			list.add(obj);

		return list;
	}

	/**
	 * Construct a new NBT compound.
	 *
	 * @return The NBT compound.
	 */
	public static NbtCompound createCompound() {
		return createRootCompound("");
	}

	/**
	 * Construct a new NMS NBT tag initialized with the given value.
	 *
	 * @param type - the NBT type.
	 * @param name - the name of the NBT tag.
	 * @param value - the value, or NULL to keep the original value.
	 * @return The created tag.
	 */
	static Object createNbtTag(NbtType type, String name, Object value) {
		List<Object> params = new ArrayList<>();
		params.add((byte)type.getId());

		if (MinecraftPackage.IS_PRE_1_8)
			params.add(name);

		Object tag = NBT_BASE.invokeMethod("createTag", null, ListUtil.toArray(params, Object.class));
		if (value != null)
			new Reflection(tag.getClass()).setValue(type.getFieldName(), tag, value);

		return tag;
	}

	/**
	 * Construct a new NBT root compound.
	 * <p>
	 * This compound must be given a name, as it is the root object.
	 *
	 * @param name - the name of the compound.
	 * @return The NBT compound.
	 */
	public static NbtCompound createRootCompound(String name) {
		return new NbtCompound(createNbtTag(NbtType.TAG_COMPOUND, name, null));
	}

	/**
	 * Construct a new NBT wrapper from a compound.
	 *
	 * @param nmsCompound - the NBT compund.
	 * @return The wrapper.
	 */
	public static NbtCompound fromCompound(Object nmsCompound) {
		return new NbtCompound(nmsCompound);
	}

	/**
	 * Construct a wrapper for an NBT tag stored (in memory) in a block. This is where
	 * auxillary data such as block data and coordinates are stored.
	 *
	 * @param block - the block.
	 * @return A copy of its NBT tag.
	 */
	public static NbtCompound fromBlockTag(Block block) {
		checkBlock(block);
		NbtCompound compound = createRootCompound("tag");

		if ((boolean)NMS_BLOCK.invokeMethod("isTileEntity", CRAFT_BLOCK.invokeMethod(NMS_BLOCK.getClazz(), block))) {
			Object craftWorld = CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = CRAFT_WORLD.invokeMethod(NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
			NMS_TILE_ENTITY.invokeMethod("save", tileEntity, compound.getHandle());
		}

		return compound;
	}

	/**
	 * Construct a wrapper for an NBT tag stored (in memory) in an item stack. This is where
	 * auxillary data such as enchanting, name and lore is stored. It does not include items
	 * material, damage value or count.
	 * <p>
	 * The item stack must be a wrapper for a CraftItemStack.
	 *
	 * @param stack - the item stack.
	 * @return A wrapper for its NBT tag.
	 */
	public static NbtCompound fromItemTag(ItemStack stack) {
		checkItemStack(stack);
		Object nms = CRAFT_ITEM_STACK.getValue(NMS_ITEM_STACK.getClazz(), stack);
		Object tag = NMS_ITEM_STACK.invokeMethod("getTag", nms);

		if (tag == null) {
			NbtCompound compound = createRootCompound("tag");
			setItemTag(stack, compound);
			return compound;
		}

		return fromCompound(tag);
	}

	/**
	 * Construct a new NBT wrapper from a list.
	 *
	 * @param nmsList - the NBT list.
	 * @return The wrapper.
	 */
	public static NbtList fromList(Object nmsList) {
		return new NbtList(nmsList);
	}

	/**
	 * Load the content of a file from a stream.
	 * <p>
	 * Use {@link Files#newInputStreamSupplier(java.io.File)} to provide a stream from a file.
	 *
	 * @param stream - the stream supplier.
	 * @param option - whether or not to decompress the input stream.
	 * @return The decoded NBT compound.
	 */
	public static NbtCompound fromStream(InputSupplier<? extends InputStream> stream, StreamOptions option) throws IOException {
		try (InputStream inputStream = stream.getInput()) {
			try (BufferedInputStream bufferedInput = new BufferedInputStream(StreamOptions.GZIP_COMPRESSION == option ? new GZIPInputStream(inputStream) : inputStream)) {
				try (DataInputStream dataInput = new DataInputStream(bufferedInput)) {
					NbtCompound compound = createRootCompound("tag");

					List<Object> params = new ArrayList<>();
					params.add(dataInput);

					if (!MinecraftPackage.IS_PRE_1_8) {
						params.add(512);
						params.add(NBT_READ_NOLIMIT);
					}

					return fromCompound(NBT_BASE.invokeMethod(Void.class, (MinecraftPackage.IS_PRE_1_8 ? null : compound.getHandle()), ListUtil.toArray(params, Object.class)));
				}
			}
		}
	}

	/**
	 * Retrieve the NBT class value.
	 *
	 * @param type - the NBT type.
	/* @param base - the NBT class instance.
	 * @return The corresponding value.
	 */
	@SuppressWarnings("unchecked")
	static <T> T getDataField(NbtType type, Object base) {
		return (T)new Reflection(base.getClass()).getValue(type.getFieldName(), base);
	}

	/**
	 * Retrieve the NBT type from a given NMS NBT tag.
	 *
	 * @param nms - the native NBT tag.
	 * @return The corresponding type.
	 */
	static NbtType getNbtType(Object nms) {
		return NBT_ENUM.get((int)(byte)NBT_BASE.invokeMethod("getTypeId", nms));
	}

	/**
	 * Retrieve the nearest NBT type for a given primitive type.
	 *
	 * @param primitive - the primitive type.
	 * @return The corresponding type.
	 */
	static NbtType getPrimitiveType(Object primitive) {
		NbtType type = NBT_ENUM.get(NBT_CLASS.inverse().get(Primitives.unwrap(primitive.getClass())));

		if (type == null)
			throw new IllegalArgumentException(StringUtil.format("Illegal type: {0} ({1})", primitive.getClass(), primitive));

		return type;
	}

	/**
	 * Retrieve a CraftItemStack version of the stack.
	 *
	 * @param stack - the stack to convert.
	 * @return The CraftItemStack version.
	 */
	public static ItemStack getCraftItemStack(ItemStack stack) {
		return (stack == null || CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass())) ? stack : (ItemStack)CRAFT_ITEM_STACK.invokeMethod("asCraftCopy", null, stack);
	}

	static Map<String, Object> getDataMap(Object handle) {
		return getDataField(NbtType.TAG_COMPOUND, handle);
	}

	static <T> List<T> getDataList(Object handle) {
		return getDataField(NbtType.TAG_LIST, handle);
	}

	/**
	 * Save the content of a NBT compound to a stream.
	 * <p>
	 * Use {@link Files#newOutputStreamSupplier(java.io.File)} to provide a stream supplier to a file.
	 *
	 * @param source - the NBT compound to save.
	 * @param stream - the stream.
	 * @param option - whether or not to compress the output.
	 */
	public static void saveStream(NbtCompound source, OutputSupplier<? extends OutputStream> stream, StreamOptions option) throws IOException {
		try (OutputStream outputStream = stream.getOutput()) {
			try (DataOutputStream dataOutput = new DataOutputStream(StreamOptions.GZIP_COMPRESSION == option ? new GZIPOutputStream(outputStream) : outputStream)) {
				List<Object> params = new ArrayList<>();

				if (MinecraftPackage.IS_PRE_1_8)
					params.add(source.getHandle());

				params.add(dataOutput);

				new Reflection(source.getHandle().getClass()).invokeMethod((String)null, (MinecraftPackage.IS_PRE_1_8 ? null : source.getHandle()), ListUtil.toArray(params, Object.class));
			}
		}
	}

	/**
	 * Set the NBT compound tag of a given item stack.
	 * <p>
	 * The item stack must be a wrapper for a CraftItemStack. Use
	 * {@link NbtFactory#getCraftItemStack(ItemStack)} if not.
	 *
	 * @param stack - the item stack, cannot be air.
	 * @param compound - the new NBT compound, or NULL to remove it.
	 * @throws IllegalArgumentException If the stack is not a CraftItemStack, or it represents air.
	 */
	public static void setItemTag(ItemStack stack, NbtCompound compound) {
		checkItemStack(stack);
		Object nms = CRAFT_ITEM_STACK.getValue(NMS_ITEM_STACK.getClazz(), stack);
		NMS_ITEM_STACK.invokeMethod("setTag", nms, compound.getHandle());
	}

	/**
	 * Set the NBT compound tag of a given block.
	 * <p>
	 * The item stack must be a wrapper for a CraftBlock.
	 *
	 * @param block - the block, must be a TileEntity.
	 * @param compound - the updated NBT compound.
	 * @throws IllegalArgumentException If the stack is not a CraftBlock.
	 */
	public static void setBlockTag(Block block, NbtCompound compound) {
		checkBlock(block);

		if ((boolean)NMS_BLOCK.invokeMethod("isTileEntity", CRAFT_BLOCK.invokeMethod(NMS_BLOCK.getClazz(), block))) {
			compound.put("x", block.getX());
			compound.put("y", block.getY());
			compound.put("z", block.getZ());
			Object craftWorld = CRAFT_WORLD.getClazz().cast(block.getWorld());
			Object tileEntity = CRAFT_WORLD.invokeMethod(NMS_TILE_ENTITY.getClazz(), craftWorld, block.getX(), block.getY(), block.getZ());
			NMS_TILE_ENTITY.invokeMethod("a", tileEntity, compound.getHandle());
		}
	}

	/**
	 * Convert wrapped List and Map objects into their respective NBT counterparts.
	 *
	 * @param name - the name of the NBT element to create.
	 * @param value - the value of the element to create. Can be a List or a Map.
	 * @return The NBT element.
	 */
	static Object unwrapValue(String name, Object value) {
		if (value == null)
			return null;

		if (value instanceof Wrapper)
			return ((Wrapper)value).getHandle();
		else
			return createNbtTag(getPrimitiveType(value), name, value);
	}

	/**
	 * Convert a given NBT element to a primitive wrapper or List/Map equivalent.
	 * <p>
	 * All changes to any mutable objects will be reflected in the underlying NBT element(s).
	 *
	 * @param nms - the NBT element.
	 * @return The wrapper equivalent.
	 */
	static Object wrapNative(Object nms) {
		if (nms == null)
			return null;

		if (NBT_BASE.getClazz().isAssignableFrom(nms.getClass())) {
			final NbtType type = getNbtType(nms);

			switch (type) {
				case TAG_COMPOUND:
					return new NbtCompound(nms);
				case TAG_LIST:
					return new NbtList(nms);
				default:
					return getDataField(type, nms);
			}
		}

		throw new IllegalArgumentException(StringUtil.format("Unexpected type: {0}", nms));
	}

	public enum StreamOptions {
		NO_COMPRESSION,
		GZIP_COMPRESSION,
	}

}