package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.AbstractList;
import java.util.List;

/**
 * Represents a list that wraps another list and converts elements
 * of its type and another exposed type.
 */
class ConvertedList extends AbstractList<Object> implements Wrapper {

	private final Object handle;
	private final List<Object> original;
	private final CachedNativeWrapper cache = new CachedNativeWrapper();

	public ConvertedList(Object handle, List<Object> original) {
		if (NbtFactory.NBT_LIST_TYPE == null)
			NbtFactory.NBT_LIST_TYPE = NbtFactory.NBT_TAG_COMPOUND.getField("type");

		this.handle = handle;
		this.original = original;
	}

	protected Object wrapOutgoing(Object value) {
		return cache.wrap(value);
	}
	protected Object unwrapIncoming(Object wrapped) {
		return NbtFactory.unwrapValue("", wrapped);
	}

	@Override
	public Object get(int index) {
		return wrapOutgoing(original.get(index));
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public Object set(int index, Object element) {
		return wrapOutgoing(
				original.set(index, unwrapIncoming(element))
		);
	}

	@Override
	public void add(int index, Object element) {
		Object nbt = unwrapIncoming(element);

		// Set the list type if its the first element
		if (size() == 0)
			NbtFactory.setFieldValue(NbtFactory.NBT_LIST_TYPE, handle, (byte)NbtFactory.getNbtType(nbt).getId());

		original.add(index, nbt);
	}

	@Override
	public Object remove(int index) {
		return wrapOutgoing(original.remove(index));
	}

	@Override
	public boolean remove(Object o) {
		return original.remove(unwrapIncoming(o));
	}

	@Override
	public Object getHandle() {
		return handle;
	}

}