package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.AbstractList;
import java.util.Iterator;
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
		this.handle = handle;
		this.original = original;
	}

	@Override
	public boolean add(Object element) {
		Object nbt = unwrapIncoming(element);

		// Set the list type if its the first element
		if (size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(byte.class, this.handle, (byte)NbtFactory.getNbtType(nbt).getId());

		return this.original.add(nbt);
	}

	@Override
	public void add(int index, Object element) {
		Object nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(byte.class, this.handle, (byte)NbtFactory.getNbtType(nbt).getId());

		this.original.add(index, nbt);
	}

	@Override
	public Object get(int index) {
		return this.wrapOutgoing(this.original.get(index));
	}

	@Override
	public Object getHandle() {
		return this.handle;
	}

	@Override
	public Iterator<Object> iterator() {
		final Iterator<Object> proxy = this.original.iterator();

		return new Iterator<Object>() {

			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public Object next() {
				return wrapOutgoing(proxy.next());
			}

			@Override
			public void remove() {
				proxy.remove();
			}

		};
	}

	@Override
	public Object remove(int index) {
		return this.wrapOutgoing(this.original.remove(index));
	}

	@Override
	public boolean remove(Object o) {
		return this.original.remove(this.unwrapIncoming(o));
	}

	@Override
	public int size() {
		return this.original.size();
	}

	@Override
	public Object set(int index, Object element) {
		return this.wrapOutgoing(this.original.set(index, this.unwrapIncoming(element)));
	}

	protected Object wrapOutgoing(Object value) {
		return this.cache.wrap(value);
	}

	protected Object unwrapIncoming(Object wrapped) {
		return NbtFactory.unwrapValue("", wrapped);
	}

}