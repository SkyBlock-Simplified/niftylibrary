package net.netcoding.niftybukkit.minecraft.nbt;

/**
 * Represents a root NBT list.
 * See also:
 * <ul>
 *   <li>{@link NbtFactory#createList}</li>
 *   <li>{@link NbtFactory#fromList}</li>
 * </ul>
 */
@SuppressWarnings("unchecked")
public final class NbtList<T> extends WrappedList<T> {

	NbtList(Object handle) {
		super(handle, NbtFactory.<T>getDataList(handle));
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public NbtList<T> clone() {
		return NbtFactory.createList(this);
	}

}