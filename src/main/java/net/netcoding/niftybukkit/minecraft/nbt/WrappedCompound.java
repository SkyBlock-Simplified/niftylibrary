package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.Map;

abstract class WrappedCompound<W> extends NbtCompound {

	private final W wrapped;

	WrappedCompound(W wrapped, Object handle) {
		super(handle);
		this.wrapped = wrapped;
	}

	@Override
	public void clear() {
		super.clear();
		this.save();
	}

	protected final W getWrapped() {
		return this.wrapped;
	}

	protected abstract void load();

	@Override
	public Object put(String key, Object value) {
		Object oldValue = super.put(key, value);
		this.save();
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ?> map) {
		super.putAll(map);
		this.save();
	}

	@Override
	public NbtCompound putPath(String path, Object value) {
		NbtCompound oldValue = super.putPath(path, value);
		this.save();
		return oldValue;
	}

	@Override
	public Object remove(Object key) {
		Object oldValue = super.remove(key);
		this.save();
		return oldValue;
	}

	@Override
	public <T> T remove(String key) {
		T oldValue = super.remove(key);
		this.save();
		return oldValue;
	}

	@Override
	public NbtCompound removePath(String path) {
		NbtCompound oldValue = super.removePath(path);
		this.save();
		return oldValue;
	}

	protected abstract void save();

}