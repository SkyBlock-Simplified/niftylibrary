package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a list that wraps another list and converts elements
 * of its type and another exposed type.
 */
@SuppressWarnings("unchecked")
abstract class WrappedList<T> extends AbstractList<T> implements Wrapper {

	private boolean isBoolList = false;
	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final List<T> original;
	private final Object handle;

	public WrappedList(Object handle, List<T> original) {
		this.handle = handle;
		this.original = original;
	}

	@Override
	public boolean add(T element) {
		T nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(byte.class, this.handle, (byte)NbtFactory.getNbtType(nbt).getId());

		return this.original.add(nbt);
	}

	@Override
	public void add(int index, T element) {
		T nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(byte.class, this.handle, (byte)NbtFactory.getNbtType(nbt).getId());

		this.original.add(index, nbt);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean changed = false;

		for (T element : collection)
			changed = changed || this.add(element);

		return changed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection) {
		int previousSize = this.size();
		int start = -1;

		for (T element : collection)
			this.add(index + (start = start + 1), element);

		return this.size() > previousSize;
	}

	@Override
	public T get(int index) {
		return this.wrapOutgoing(this.original.get(index));
	}

	@Override
	public final Object getHandle() {
		return this.handle;
	}

	@Override
	public Iterator<T> iterator() {
		final Iterator<T> proxy = this.original.iterator();

		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public T next() {
				return WrappedList.this.wrapOutgoing(proxy.next());
			}

			@Override
			public void remove() {
				proxy.remove();
			}

		};
	}

	@Override
	public T remove(int index) {
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
	public T set(int index, T element) {
		return this.wrapOutgoing(this.original.set(index, this.unwrapIncoming(element)));
	}

	protected T wrapOutgoing(Object value) {
		return (T)this.cache.wrap(this.isBoolList ? (byte)value > 0 : value);
	}

	protected T unwrapIncoming(Object wrapped) {
		if ((wrapped instanceof Boolean) && !this.isBoolList)
			this.isBoolList = true;

		return (T)NbtFactory.unwrapValue("", (this.isBoolList ? (byte)((boolean)wrapped ? 1 : 0) : wrapped));
	}

}