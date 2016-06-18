package net.netcoding.niftybukkit._new_.api.nbt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import com.google.common.primitives.Primitives;
import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.minecraft.block.Block;
import net.netcoding.niftybukkit._new_.minecraft.entity.Entity;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.reflection.BukkitReflection;
import net.netcoding.niftybukkit._new_.reflection.MinecraftPackage;
import net.netcoding.niftybukkit._new_.reflection.MinecraftProtocol;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings({ "unchecked", "deprecation" })
public abstract class NbtFactory<I extends ItemStack, B extends Block> {

	// https://bukkit.org/threads/library-edit-or-create-nbt-tags-with-a-compact-class-no-obc-nms.178464/
	// https://gist.github.com/aadnk/6753244 (Nov 3, 2013)

	// Convert between NBT id and the equivalent class in java
	static final BiMap<Byte, Class<?>> NBT_CLASS = HashBiMap.create();
	static final BiMap<Byte, NbtType> NBT_ENUM = HashBiMap.create();

	// Reflection
	//static final Reflection CRAFT_ITEM_STACK = new BukkitReflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
	//static final Reflection CRAFT_WORLD = new BukkitReflection("CraftWorld", MinecraftPackage.CRAFTBUKKIT);
	//static final Reflection CRAFT_BLOCK = new BukkitReflection("CraftBlock", "block", MinecraftPackage.CRAFTBUKKIT);
	public static final Reflection NBT_BASE = BukkitReflection.getCompatibleForgeReflection("NBTBase", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	public static final Reflection NBT_READ_LIMITER = BukkitReflection.getCompatibleForgeReflection((MinecraftProtocol.isForge() ? "NBTSizeTracker" : "NBTReadLimiter"), MinecraftPackage.MINECRAFT_SERVER, "nbt");
	public static final Reflection NBT_TAG_COMPOUND = BukkitReflection.getCompatibleForgeReflection("NBTTagCompound", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	public static final Reflection NBT_TAG_LIST = BukkitReflection.getCompatibleForgeReflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER, "nbt");
	protected static final Object NBT_READ_NOLIMIT = NBT_READ_LIMITER.getValue(NBT_READ_LIMITER.getClazz(), null);
	public static final Reflection NMS_ITEM_STACK = BukkitReflection.getCompatibleForgeReflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER, "item");
	public static final Reflection NMS_TILE_ENTITY = BukkitReflection.getCompatibleForgeReflection("TileEntity", MinecraftPackage.MINECRAFT_SERVER, "tileentity");
	public static final Reflection NMS_BLOCK = new BukkitReflection("Block", MinecraftPackage.MINECRAFT_SERVER);

	protected NbtFactory() { }

	static Object adjustIncoming(Object value) {
		if (value == null)
			return null;

		Class<?> clazz = Primitives.unwrap(value.getClass());

		if (WrappedList.class.isAssignableFrom(clazz) || WrappedMap.class.isAssignableFrom(clazz))
			return value;

		if (!NBT_CLASS.inverse().containsKey(clazz)) {
			if (clazz.isArray())
				value = Nifty.getNbtFactory().createList((Object[])value);
			else {
				if (value instanceof Boolean)
					value = (byte) ((boolean) value ? 1 : 0);
				else if (CharSequence.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (UUID.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (BigDecimal.class.isAssignableFrom(clazz))
					value = ((BigDecimal)value).doubleValue();
				else if (BigInteger.class.isAssignableFrom(clazz))
					value = ((BigInteger)value).longValue();
				else if (clazz.isEnum())
					value = ((Enum)value).name();
				else if (Collection.class.isAssignableFrom(clazz))
					value = Nifty.getNbtFactory().createList((Collection<?>)value);
				else if (Map.class.isAssignableFrom(clazz)) {
					NbtCompound compound = Nifty.getNbtFactory().createCompound();
					compound.putAll((Map<String, Object>)value);
					value = compound;
				}
			}
		}

		return value;
	}

	static Object adjustOutgoing(Object value, Class clazz) {
		if (value == null)
			return null;

		if (clazz != null) {
			if (boolean.class.equals(clazz))
				value = (byte)value > 0;
			else if (UUID.class.equals(clazz))
				value = UUID.fromString(value.toString());
			else if (BigDecimal.class.equals(clazz))
				value = BigDecimal.valueOf((double)value);
			else if (BigInteger.class.equals(clazz))
				value = BigInteger.valueOf((long)value);
			else if (clazz.isEnum())
				value = Enum.valueOf(clazz, value.toString());
			else if (Map.class.isAssignableFrom(clazz)) {
				NbtCompound compound = (NbtCompound)value;
				boolean adjusted = false;

				if (!Map.class.equals(clazz)) {
					Reflection refCollection = new Reflection(clazz);
					Map<String, Object> map = (Map<String, Object>)refCollection.newInstance();
					refCollection.invokeMethod("putAll", map, (Map)compound);
					adjusted = true;
				}

				if (!adjusted)
					value = compound;
			} else if (Collection.class.isAssignableFrom(clazz) || clazz.isArray()) {
				NbtList nbtList = (NbtList)value;

				if (!clazz.isArray()) {
					boolean adjusted = false;

					if (!Collection.class.equals(clazz)) {
						Reflection refCollection = new Reflection(clazz);
						Object collection = refCollection.newInstance();
						refCollection.invokeMethod("addAll", collection, (Collection)nbtList);
						adjusted = true;
					}

					if (!adjusted)
						value = nbtList;
				} else
					value = ListUtil.toArray(nbtList, clazz.getComponentType());
			}
		}

		return value;
	}

	/**
	 * Ensure that the given item can store NBT information.
	 *
	 * @param item - The item to check.
	 */
	protected abstract void checkItem(I item);

	/**
	 * Ensure that the given block can store NBT information.
	 *
	 * @param block - The item to check.
	 */
	protected abstract void checkBlock(Block block);

	/**
	 * Ensure that the given entity can store NBT information.
	 *
	 * @param entity - The item to check.
	 */
	protected abstract void checkEntity(Entity entity);

	/**
	 * Construct a new NBT list of an unspecified type.
	 *
	 * @return The NBT list.
	 */
	@SafeVarargs
	public final <T> NbtList<T> createList(T... content) {
		return createList(Arrays.asList(content));
	}

	/**
	 * Construct a new NBT list of an unspecified type.
	 *
	 * @return The NBT list.
	 */
	public final <T> NbtList<T> createList(Iterable<T> iterable) {
		NbtList<T> list = new NbtList<>(createNbtTag(NbtType.TAG_LIST, null));

		for (T obj : iterable)
			list.add(obj);

		return list;
	}

	/**
	 * Construct a new NBT compound.
	 *
	 * @return The NBT compound.
	 */
	public final NbtCompound createCompound() {
		return new NbtCompound(createRootNativeCompound());
	}

	/**
	 * Construct a new NMS NBT tag initialized with the given value.
	 *
	 * @param type - the NBT type.
	 * @param value - the value, or NULL to keep the original value.
	 * @return The created tag.
	 */
	private static Object createNbtTag(NbtType type, Object value) {
		Object tag = NBT_BASE.invokeMethod(NBT_BASE.getClazz(), null, type.getId());

		if (value != null)
			new Reflection(tag.getClass()).setValue(type.getFieldType(), tag, value);

		return tag;
	}

	protected static Object createRootNativeCompound() {
		return createNbtTag(NbtType.TAG_COMPOUND, null);
	}

	/**
	 * Construct a new NBT wrapper from a compound.
	 *
	 * @param nmsCompound - the NBT compund.
	 * @return The wrapper.
	 */
	public final NbtCompound fromCompound(Object nmsCompound) {
		return new NbtCompound(nmsCompound);
	}

	/**
	 * Construct a wrapper for an NBT tag stored (in memory) in a block. This is where
	 * auxillary data such as block data and coordinates are stored.
	 *
	 * @param block - the block.
	 * @return A copy of its NBT tag.
	 */
	public final NbtBlockCompound fromBlockTag(B block) {
		this.checkBlock(block);
		return this.fromBlockTag0(block);
	}

	protected abstract NbtBlockCompound fromBlockTag0(B block);

	protected final NbtEntityCompound fromEntityTag(Entity entity) {
		this.checkEntity(entity);
		return new NbtEntityCompound(entity, createRootNativeCompound());
	}

	/**
	 * Construct a wrapper for an NBT tag stored (in memory) in an item stack. This is where
	 * auxillary data such as enchanting, name and lore is stored. It does not include items
	 * material, damage value or count.
	 *
	 * @param stack - the item stack.
	 * @return A wrapper for its NBT tag.
	 */
	public final NbtItemCompound fromItemTag(I stack) {
		this.checkItem(stack);
		return this.fromItemTag0(stack);
	}

	protected abstract NbtItemCompound fromItemTag0(I item);

	/**
	 * Construct a new NBT wrapper from a list.
	 *
	 * @param nmsList - the NBT list.
	 * @return The wrapper.
	 */
	public final NbtList fromList(Object nmsList) {
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
	public final NbtCompound fromStream(InputSupplier<? extends InputStream> stream, StreamOptions option) throws IOException {
		try (InputStream inputStream = stream.getInput()) {
			try (BufferedInputStream bufferedInput = new BufferedInputStream(StreamOptions.GZIP_COMPRESSION == option ? new GZIPInputStream(inputStream) : inputStream)) {
				try (DataInputStream dataInput = new DataInputStream(bufferedInput)) {
					NbtCompound compound = this.createCompound();
					return this.fromCompound(NBT_BASE.invokeMethod(Void.class, (MinecraftProtocol.isPre1_8() ? null : compound.getHandle()), dataInput, 512, NBT_READ_NOLIMIT));
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
	protected static <T> T getDataField(NbtType type, Object base) {
		return (T)new Reflection(base.getClass()).getValue(type.getFieldType(), base);
	}

	/**
	 * Retrieve the NBT type from a given NMS NBT tag.
	 *
	 * @param nms - the native NBT tag.
	 * @return The corresponding type.
	 */
	protected static NbtType getNbtType(Object nms) {
		return NBT_ENUM.get(NBT_BASE.invokeMethod(Byte.class, nms));
	}

	/**
	 * Retrieve the nearest NBT type for a given primitive type.
	 *
	 * @param primitive - the primitive type.
	 * @return The corresponding type.
	 */
	protected static NbtType getPrimitiveType(Object primitive) {
		NbtType type = NBT_ENUM.get(NBT_CLASS.inverse().get(Primitives.unwrap(primitive.getClass())));

		if (type == null)
			throw new IllegalArgumentException(StringUtil.format("Illegal type: {0} ({1})", primitive.getClass(), primitive));

		return type;
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
				new Reflection(source.getHandle().getClass()).invokeMethod((String)null, (MinecraftProtocol.isPre1_8() ? null : source.getHandle()), dataOutput);
			}
		}
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
	public final void setBlockTag(B block, NbtCompound compound) {
		this.checkBlock(block);
		this.setBlockTag0(block, compound);
	}

	protected abstract void setBlockTag0(B block, NbtCompound compound);

	/**
	 * Set the NBT compound tag of a given item stack.
	 *
	 * @param item - the item stack, cannot be air.
	 * @param compound - the new NBT compound, or NULL to remove it.
	 * @throws IllegalArgumentException If the stack is not a CraftItemStack, or it represents air.
	 */
	public final void setItemTag(I item, NbtCompound compound) {
		this.checkItem(item);
		this.setItemTag0(item, compound);
	}

	protected abstract void setItemTag0(I item, NbtCompound compound);

	/**
	 * Convert wrapped List and Map objects into their respective NBT counterparts.
	 *
	 * @param value - the value of the element to create. Can be a List or a Map.
	 * @return The NBT element.
	 */
	protected static Object unwrapValue(Object value) {
		if (value == null)
			return null;

		if (value instanceof Wrapper)
			return ((Wrapper)value).getHandle();
		else
			return createNbtTag(getPrimitiveType(value), value);
	}

	/**
	 * Convert a given NBT element to a primitive wrapper or List/Map equivalent.
	 * <p>
	 * All changes to any mutable objects will be reflected in the underlying NBT element(s).
	 *
	 * @param nms - the NBT element.
	 * @return The wrapper equivalent.
	 */
	protected static Object wrapNative(Object nms) {
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