package net.netcoding.nifty.common.api.nbt;

import net.netcoding.nifty.common.Nifty;

/**
 * Represents a root NBT list.
 * See also:
 * <ul>
 *   <li>{@link NbtFactory#createList}</li>
 *   <li>{@link NbtFactory#fromList}</li>
 * </ul>
 */
public final class NbtList<E> extends WrappedList<E> {

	NbtList(Object handle) {
		super(handle, NbtFactory.getDataField(NbtType.TAG_LIST, handle));
	}

	@Override
	public NbtList<E> clone() {
		return Nifty.getNbtFactory().createList(this);
	}

}