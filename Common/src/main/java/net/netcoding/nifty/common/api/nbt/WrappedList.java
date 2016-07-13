package net.netcoding.nifty.common.api.nbt;

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
abstract class WrappedList<E> extends AbstractList<E> implements Wrapper {

	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final List<E> original;
	private final Object handle;
	private Class<?> support;

	public WrappedList(Object handle, List<E> original) {
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
	public boolean add(E element) {
		E nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(Byte.class, this.handle, NbtFactory.getNbtType(nbt).getId());

		return this.original.add(nbt);
	}

	@Override
	public void add(int index, E element) {
		E nbt = this.unwrapIncoming(element);

		// Set the list type if its the first element
		if (this.size() == 0)
			NbtFactory.NBT_TAG_LIST.setValue(Byte.class, this.handle, NbtFactory.getNbtType(nbt).getId());

		this.original.add(index, nbt);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean changed = false;

		for (E element : collection)
			changed = changed || this.add(element);

		return changed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		int previousSize = this.size();
		int start = -1;

		for (E element : collection)
			this.add(index + (start = start + 1), element);

		return this.size() > previousSize;
	}

	@Override
	public E get(int index) {
		return this.wrapOutgoing(this.original.get(index));
	}

	@Override
	public final Object getHandle() {
		return this.handle;
	}

	@Override
	public Iterator<E> iterator() {
		final Iterator<E> proxy = this.original.iterator();

		return new Iterator<E>() {

			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public E next() {
				return WrappedList.this.wrapOutgoing(proxy.next());
			}

			@Override
			public void remove() {
				proxy.remove();
			}

		};
	}

	@Override
	public E remove(int index) {
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
	public E set(int index, E element) {
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

	protected E wrapOutgoing(Object value) {
		return (E)NbtFactory.adjustOutgoing(this.cache.wrap(value), this.support);
	}

	protected E unwrapIncoming(Object wrapped) {
		return (E)NbtFactory.unwrapValue(this.adjustIncoming(wrapped));
	}

}