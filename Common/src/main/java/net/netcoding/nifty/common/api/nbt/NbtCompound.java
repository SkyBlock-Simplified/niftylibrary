package net.netcoding.nifty.common.api.nbt;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.util.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an NBT compound.
 * <p>
 * All changes to this map will be reflected in the underlying NBT compound. Values may only be of the following:
 * <ul>
 *   <li>Primitive types</li>
 *   <li>{@link String String}</li>
 *   <li>{@link Collection}</li>
 *   <li>{@link List}</li>
 *   <li>{@link Set}</li>
 *   <li>{@link Map}</li>
 *   <li>{@link NbtList}</li>
 *   <li>{@link NbtCompound}</li>
 * </ul>
 * <p>
 * See also:
 * <ul>
 *   <li>{@link NbtFactory#createCompound}</li>
 *   <li>{@link NbtFactory#fromCompound}</li>
 * </ul>
 */
@SuppressWarnings("unchecked")
public class NbtCompound extends WrappedMap implements Cloneable {

	NbtCompound(Object handle, boolean root) {
		super(handle, root, NbtFactory.getDataField(NbtType.TAG_COMPOUND, handle));
	}

	/**
	 * This will not be attached to anything.
	 * <p>
	 * Will only contain a copy of the current compound.
	 *
	 * @return Copy of current compound
	 */
	@Override
	public NbtCompound clone() {
		NbtCompound compound = Nifty.getNbtFactory().createCompound();
		compound.putAll(this);
		return compound;
	}

	/**
	 * Checks if the path exists in the tree.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds. The
	 * retrieval operation will return false if any of them are missing.
	 *
	 * @param path The path to the entry.
	 * @return True if found.
	 */
	public boolean containsPath(String path) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		NbtCompound current = this;

		for (String entry : entries) {
			Object child = current.get(entry);

			if (child == null)
				return false;

			if (!(child instanceof NbtCompound))
				return true;

			current = (NbtCompound)child;
		}

		return true;
	}

	/**
	 * Retrieve the value by the given key.
	 *
	 * @param key The name of the value.
	 * @return An existing or null value.
	 */
	public <T> T get(String key) {
		return this.get(key, null);
	}

	/**
	 * Retrieve the value by the given key.
	 *
	 * @param key The name of the value.
	 * @param defaultValue The default value if key doesn't exist.
	 * @return An existing or default value.
	 */
	public <T> T get(String key, T defaultValue) {
		return this.containsKey(key) ? (T)super.get(key) : defaultValue;
	}

	/**
	 * Retrieve the value of a given entry in the tree.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds. The
	 * retrieval operation will be cancelled if any of them are missing.
	 *
	 * @param path The path to the entry.
	 * @return The value, or NULL if not found.
	 */
	public <T> T getPath(String path) {
		T value = null;

		if (this.containsPath(path)) {
			List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
			NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), false);
			value = map.get(entries.get(entries.size() - 1));
		}

		return value;
	}

	/**
	 * Retrieve a map from a given path.
	 * @param path The path of compounds to look up.
	 * @param createNew Whether or not to create new compounds on the way.
	 * @return The map at this location.
	 */
	private NbtCompound getMap(Iterable<String> path, boolean createNew) {
		NbtCompound current = this;

		for (String entry : path) {
			NbtCompound child = current.get(entry);

			if (child == null) {
				if (!createNew)
					throw new IllegalArgumentException("Cannot find " + entry + " in " + path);

				current.put(entry, child = Nifty.getNbtFactory().createCompound());
			}

			current = child;
		}

		return current;
	}

	/**
	 * Retrieve the map by the given name.
	 *
	 * @param key The name of the map.
	 * @return An existing or new map.
	 */
	public NbtCompound getMap(String key) {
		return this.getMap(key, true);
	}

	/**
	 * Retrieve the map by the given name.
	 *
	 * @param key The name of the map.
	 * @param createNew Whether or not to create a new map if its missing.
	 * @return An existing map, a new map or null.
	 */
	public NbtCompound getMap(String key, boolean createNew) {
		return this.getMap(Collections.singletonList(key), createNew);
	}

	/**
	 * Set the value of an entry at a given location.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds, and will
	 * be created if they are missing.
	 *
	 * @param path The path to the entry.
	 * @param value The new value of this entry.
	 * @return This compound, for chaining.
	 */
	public NbtCompound putPath(String path, Object value) {
		List<String> entries = StringUtil.toList(StringUtil.split("\\.", path));
		NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), true);
		map.put(entries.get(entries.size() - 1), value);
		this.save();
		return this;
	}

	/**
	 * Remove the value of a given entry.
	 *
	 * @param key The name of the value.
	 * @return The previous value, or NULL if not found.
	 */
	public <T> T remove(String key) {
		return (T)super.remove(key);
	}

	/**
	 * Remove the value of a given entry in the tree.
	 * <p>
	 * Every element of the path (except the end) are assumed to be compounds. The
	 * retrieval operation will return the last most compound.
	 *
	 * @param path The path to the entry.
	 * @return The last most compound, or this compound if not found..
	 */
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

		this.save();
		return current;
	}

	/**
	 * Save the content of a NBT compound to a stream.
	 *
	 * @param stream The output stream.
	 */
	public void saveTo(OutputStream stream) throws IOException {
		NbtFactory.saveStream(this, stream);
	}

}