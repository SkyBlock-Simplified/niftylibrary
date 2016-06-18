package net.netcoding.nifty.common._new_.api.nbt;

import net.netcoding.nifty.common.Nifty;

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
		super(handle, NbtFactory.getDataField(NbtType.TAG_LIST, handle));
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override
	public NbtList<T> clone() {
		return Nifty.getNbtFactory().createList(this);
	}

}