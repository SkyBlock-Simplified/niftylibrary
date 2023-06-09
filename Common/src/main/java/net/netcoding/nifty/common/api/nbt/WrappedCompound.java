package net.netcoding.nifty.common.api.nbt;

abstract class WrappedCompound<W> extends NbtCompound {

	private final W wrapped;

	WrappedCompound(W wrapped, Object handle) {
		super(handle, true);
		this.wrapped = wrapped;
	}

	protected final W getWrapped() {
		return this.wrapped;
	}

	protected abstract void load();

}