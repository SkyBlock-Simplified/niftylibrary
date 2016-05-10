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
public final class NbtList extends ConvertedList {

	NbtList(Object handle) {
		super(handle, NbtFactory.getDataList(handle));
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public NbtList clone() {
		NbtList list = NbtFactory.createList();
		list.addAll(this);
		return list;
	}

	public <T> T get(Integer index) {
		return (T)NbtFactory.adjustValue(super.get(index));
	}

}