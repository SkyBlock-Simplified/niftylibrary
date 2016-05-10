package net.netcoding.niftybukkit.minecraft.nbt;

import net.netcoding.niftycore.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a root NBT compound.
 * <p>
 * All changes to this map will be reflected in the underlying NBT compound. Values may only be one of the following:
 * <ul>
 *   <li>Primitive types</li>
 *   <li>{@link java.lang.String String}</li>
 *   <li>{@link NbtList}</li>
 *   <li>{@link NbtCompound}</li>
 * </ul>
 * <p>
 * See also:
 * <ul>
 *   <li>{@link NbtFactory#createCompound()}</li>
 *   <li>{@link NbtFactory#fromCompound(Object)}</li>
 * </ul>
 */
@SuppressWarnings("unchecked")
public final class NbtCompound extends ConvertedMap implements Cloneable {

	NbtCompound(Object handle) {
		super(handle, NbtFactory.getDataMap(handle));
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public NbtCompound clone() {
		NbtCompound compound = NbtFactory.createRootCompound("tag");
		compound.putAll(this);
		return compound;
	}

	public boolean containsPath(String path) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		NbtCompound current = this;

		for (String entry : entries) {
			NbtCompound child = current.get(entry);

			if (child == null)
				return false;

			current = child;
		}

		return true;
	}

	public <T> T get(String key) {
		return (T)NbtFactory.adjustValue(super.get(key));
	}

	public <T> T get(String key, T defaultValue) {
		return containsKey(key) ? this.<T>get(key) : defaultValue;
	}

	/**
	 * Retrieve the list by the given name.
	 *
	 * @param key - the name of the list.
	 * @return An existing list or NULL.
	 */
	/*public NbtList getList(String key) {
		return this.getList(key, false);
	}*/

	/**
	 * Retrieve the list by the given name.
	 *
	 * @param key - the name of the list.
	 * @param createNew - whether or not to create a new list if its missing.
	 * @return An existing list, a new list or NULL.
	 */
	/*public NbtList getList(String key, boolean createNew) {
		NbtList list = this.get(key);

		if (list == null && createNew)
			this.put(key, list = NbtFactory.createList());

		return list;
	}*/

	/**
	 * Retrieve the value of a given entry in the tree.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds. The
	 * retrieval operation will be cancelled if any of them are missing.
	 *
	 * @param path - path to the entry.
	 * @return The value, or NULL if not found.
	 */
	public <T> T getPath(String path) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), false);

		if (map != null) {
			Object value = map.get(entries.get(entries.size() - 1));
			return (T)NbtFactory.adjustValue(value);
		}

		return null;
	}

	/**
	 * Retrieve a map from a given path.
	 * @param path - path of compounds to look up.
	 * @param createNew - whether or not to create new compounds on the way.
	 * @return The map at this location.
	 */
	private NbtCompound getMap(Iterable<String> path, boolean createNew) {
		NbtCompound current = this;

		for (String entry : path) {
			NbtCompound child = current.get(entry);

			if (child == null) {
				if (!createNew)
					throw new IllegalArgumentException("Cannot find " + entry + " in " + path);

				current.put(entry, child = NbtFactory.createCompound());
			}

			current = child;
		}

		return current;
	}

	/**
	 * Retrieve the map by the given name.
	 * @param key - the name of the map.
	 * @return An existing or new map.
	 */
	public NbtCompound getMap(String key) {
		return this.getMap(key, true);
	}

	/**
	 * Retrieve the map by the given name.
	 * @param key - the name of the map.
	 * @param createNew - whether or not to create a new map if its missing.
	 * @return An existing map, a new map or NULL.
	 */
	public NbtCompound getMap(String key, boolean createNew) {
		return this.getMap(Collections.singletonList(key), createNew);
	}

	/**
	 * Set the value of an entry at a given location.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds, and will
	 * be created if they are missing.
	 * @param path - the path to the entry.
	 * @param value - the new value of this entry.
	 * @return This compound, for chaining.
	 */
	public NbtCompound putPath(String path, Object value) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		Map<String, Object> map = this.getMap(entries.subList(0, entries.size() - 1), true);
		map.put(entries.get(entries.size() - 1), value);
		return this;
	}

	public NbtCompound removePath(String path) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		NbtCompound current = this;

		for (int i = 0; i < entries.size(); i++) {
			String entry = entries.get(i);

			if (i == entries.size() - 1)
				current.remove(entry);
			else
				current = current.get(entry);
		}

		return current;
	}

}