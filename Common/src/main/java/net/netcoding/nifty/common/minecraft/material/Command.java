package net.netcoding.nifty.common.minecraft.material;

public interface Command extends MaterialData, OptionalRedstone {

	@Override
	Command clone();

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x1) != 0;
	}

	@Override
	default void setPowered(boolean value) {
		this.setData((byte) (value ? (this.getData() | 1) : (this.getData() & -2)));
	}

}