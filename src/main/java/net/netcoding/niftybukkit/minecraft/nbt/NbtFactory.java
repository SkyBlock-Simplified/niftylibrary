package net.netcoding.niftybukkit.minecraft.nbt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Primitives;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NbtFactory {

	// https://bukkit.org/threads/library-edit-or-create-nbt-tags-with-a-compact-class-no-obc-nms.178464/
	// https://gist.github.com/aadnk/6753244 (Nov 3, 2013)

	// Convert between NBT id and the equivalent class in java
	static final BiMap<Integer, Class<?>> NBT_CLASS = HashBiMap.create();
	static final BiMap<Integer, NbtType> NBT_ENUM = HashBiMap.create();

	// Reflection
	static final Reflection CRAFT_ITEM_STACK = new Reflection("CraftItemStack", "inventory", MinecraftPackage.CRAFTBUKKIT);
	static final Reflection NMS_ITEM_STACK = new Reflection("ItemStack", MinecraftPackage.MINECRAFT_SERVER);
	static final Reflection NBT_TAG_LIST = new Reflection("NBTTagList", MinecraftPackage.MINECRAFT_SERVER);
	static final Reflection NBT_BASE = new Reflection("NBTBase", MinecraftPackage.MINECRAFT_SERVER);

	// The NBT base class
	static final Field[] DATA_FIELD = new Field[12];

	/**
	 * Ensure that the given stack can store arbitrary NBT information.
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

	/**
	 * Construct a new NBT list of an unspecified type.
	 * @return The NBT list.
	 */
	public static NbtList createList(Object... content) {
		return createList(Arrays.asList(content));
	}

	/**
	 * Construct a new NBT list of an unspecified type.
	 * @return The NBT list.
	 */
	public static NbtList createList(Iterable<?> iterable) {
		NbtList list = new NbtList(createNbtTag(NbtType.TAG_LIST, "", null));

		// Add the content as well
		for (Object obj : iterable)
			list.add(obj);

		return list;
	}

	/**
	 * Construct a new NBT compound.
	 * <p>
	 * Use {@link NbtCompound#getMap} to modify it.
	 * @return The NBT compound.
	 */
	public static NbtCompound createCompound() {
		return createRootCompound("");
	}

	/**
	 * Construct a new NMS NBT tag initialized with the given value.
	 * @param type - the NBT type.
	 * @param name - the name of the NBT tag.
	 * @param value - the value, or NULL to keep the original value.
	 * @return The created tag.
	 */
	static Object createNbtTag(NbtType type, String name, Object value) {
		List<Object> params = new ArrayList<>();
		NbtType newType = (NbtType.TAG_BOOLEAN == type ? NbtType.TAG_INT : type);
		params.add((byte)newType.getId());

		if (MinecraftPackage.IS_PRE_1_8)
			params.add(name);

		Object tag = NBT_BASE.invokeMethod("createTag", null, ListUtil.toArray(params, Object.class));

		if (value != null) {
			// Handles Unsupported Types
			Object newValue = (NbtType.TAG_BOOLEAN == type ? (type.getId() + ((boolean)value ? 1 : 0)) : value);
			newValue = (NbtType.TAG_ARRAY == type ? NbtFactory.createList((Object[])newValue) : newValue);
			newValue = (NbtType.TAG_COLLECTION == type ? NbtFactory.createList((Collection<?>)newValue) : newValue);
			newValue = (NbtType.TAG_SET == type ? NbtFactory.createList((Set<?>)newValue) : newValue);
			setFieldValue(getDataField(newType, tag), tag, newValue);
		}

		return tag;
	}

	/**
	 * Construct a new NBT root compound.
	 * <p>
	 * This compound must be given a name, as it is the root object.
	 * @param name - the name of the compound.
	 * @return The NBT compound.
	 */
	public static NbtCompound createRootCompound(String name) {
		return new NbtCompound(createNbtTag(NbtType.TAG_COMPOUND, name, null));
	}

	/**
	 * Construct a new NBT wrapper from a compound.
	 * @param nmsCompound - the NBT compund.
	 * @return The wrapper.
	 */
	public static NbtCompound fromCompound(Object nmsCompound) {
		return new NbtCompound(nmsCompound);
	}

	/**
	 * Construct a wrapper for an NBT tag stored (in memory) in an item stack. This is where
	 * auxillary data such as enchanting, name and lore is stored. It does not include items
	 * material, damage value or count.
	 * <p>
	 * The item stack must be a wrapper for a CraftItemStack.
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
	 * @param nmsList - the NBT list.
	 * @return The wrapper.
	 */
	public static NbtList fromList(Object nmsList) {
		return new NbtList(nmsList);
	}

	/**
	 * Retrieve the field where the NBT class stores its value.
	 * @param type - the NBT type.
	/* @param base - the NBT class instance.
	 * @return The corresponding field.
	 */
	static Field getDataField(NbtType type, Object base) {
		if (DATA_FIELD[type.getId()] == null)
			DATA_FIELD[type.getId()] = new Reflection(base.getClass()).getField(type.getFieldName());

		return DATA_FIELD[type.getId()];
	}

	/**
	 * Retrieve the NBT type from a given NMS NBT tag.
	 * @param nms - the native NBT tag.
	 * @return The corresponding type.
	 */
	static NbtType getNbtType(Object nms) {
		return NBT_ENUM.get((int)(byte)NBT_BASE.invokeMethod("getTypeId", nms));
	}

	/**
	 * Retrieve the nearest NBT type for a given primitive type.
	 * @param primitive - the primitive type.
	 * @return The corresponding type.
	 */
	static NbtType getPrimitiveType(Object primitive) {
		NbtType type = NBT_ENUM.get(NBT_CLASS.inverse().get(Primitives.unwrap(primitive.getClass())));

		// Display the illegal value at least
		if (type == null)
			throw new IllegalArgumentException(StringUtil.format("Illegal type: {0} ({1})", primitive.getClass(), primitive));

		return type;
	}

	/**
	 * Retrieve a CraftItemStack version of the stack.
	 * @param stack - the stack to convert.
	 * @return The CraftItemStack version.
	 */
	public static ItemStack getCraftItemStack(ItemStack stack) {
		return (stack == null || CRAFT_ITEM_STACK.getClazz().isAssignableFrom(stack.getClass())) ? stack : (ItemStack)CRAFT_ITEM_STACK.invokeMethod("asCraftCopy", null, stack);
	}

	@SuppressWarnings("unchecked")
	static Map<String, Object> getDataMap(Object handle) {
		return (Map<String, Object>)getFieldValue(getDataField(NbtType.TAG_COMPOUND, handle), handle);
	}

	@SuppressWarnings("unchecked")
	static List<Object> getDataList(Object handle) {
		return (List<Object>)getFieldValue(getDataField(NbtType.TAG_LIST, handle), handle);
	}

	/**
	 * Set the NBT compound tag of a given item stack.
	 * <p>
	 * The item stack must be a wrapper for a CraftItemStack. Use
	 * {@link NbtFactory#getCraftItemStack(ItemStack)} if not.
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
	 * Convert wrapped List and Map objects into their respective NBT counterparts.
	 * @param name - the name of the NBT element to create.
	 * @param value - the value of the element to create. Can be a List or a Map.
	 * @return The NBT element.
	 */
	static Object unwrapValue(String name, Object value) {
		if (value == null)
			return null;

		if (value instanceof Wrapper)
			return ((Wrapper) value).getHandle();
		else if (value instanceof List)
			throw new IllegalArgumentException("Can only insert a WrappedList.");
		else if (value instanceof Map)
			throw new IllegalArgumentException("Can only insert a WrappedCompound.");
		else
			return createNbtTag(getPrimitiveType(value), name, value);
	}

	/**
	 * Convert a given NBT element to a primitive wrapper or List/Map equivalent.
	 * <p>
	 * All changes to any mutable objects will be reflected in the underlying NBT element(s).
	 * @param nms - the NBT element.
	 * @return The wrapper equivalent.
	 */
	static Object wrapNative(Object nms) {
		if (nms == null)
			return null;

		if (NBT_BASE.getClazz().isAssignableFrom(nms.getClass())) {
			final NbtType type = getNbtType(nms);

			// Handle the different types
			switch (type) {
				case TAG_COMPOUND:
					return new NbtCompound(nms);
				case TAG_LIST:
					return new NbtList(nms);
				default:
					return getFieldValue(getDataField(type, nms), nms);
			}
		}

		throw new IllegalArgumentException(StringUtil.format("Unexpected type: {0}", nms));
	}

	static void setFieldValue(Field field, Object target, Object value) {
		try {
			field.set(target, value);
		} catch (Exception ex) {
			throw new RuntimeException(StringUtil.format("Unable to set {0} for {1}!", field, target), ex);
		}
	}

	static Object getFieldValue(Field field, Object target) {
		try {
			return field.get(target);
		} catch (Exception ex) {
			throw new RuntimeException(StringUtil.format("Unable to get {0} for {1}!", field, target), ex);
		}
	}

}