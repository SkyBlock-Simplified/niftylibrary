package net.netcoding.niftybukkit._new_.api.nbt;

import com.google.common.primitives.Primitives;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a list that wraps another list and converts elements
 * of its type and another exposed type.
 */
@SuppressWarnings("unchecked")
abstract class WrappedList<T> extends AbstractList<T> implements Wrapper {

	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final List<T> original;
	private final Object handle;
	private Class<?> support;

	public WrappedList(Object handle, List<T> original) {
		this.handle = handle;
		this.original = original;
	}

	private Object adjustIncoming(Object value) {
		Object adjusted = NbtFactory.adjustIncoming(value);

		if (!Objects.equals(adjusted, value))
			this.support = Primitives.unwrap(value.getClass());

		return adjusted;
	}

	@Override
	public boolean add(T element) {
		T nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(Byte.class, this.handle, NbtFactory.getNbtType(nbt).getId());

		return this.original.add(nbt);
	}

	@Override
	public void add(int index, T element) {
		T nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(Byte.class, this.handle, NbtFactory.getNbtType(nbt).getId());

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

	public String serialize() {
		List<Object> output = new ArrayList<>();

		for (Object value : this) {
			Object newValue = value;

			if (WrappedMap.class.isAssignableFrom(value.getClass()))
				newValue = ((WrappedMap)newValue).serialize();

			output.add(newValue);
		}

		return output.toString();
	}

	@Override
	public String toString() {
		return this.serialize();
	}

	protected T wrapOutgoing(Object value) {
		return (T)NbtFactory.adjustOutgoing(this.cache.wrap(value), this.support);
	}

	protected T unwrapIncoming(Object wrapped) {
		return (T)NbtFactory.unwrapValue(this.adjustIncoming(wrapped));
	}

}