package net.netcoding.nifty.common.minecraft.material;

public interface Sapling extends Wood {

	@Override
	Sapling clone();

	default boolean isInstantGrowable() {
		return (this.getData() & 0x8) == 0x8;
	}

	default void setInstantGrowable(boolean value) {
		this.setData((byte) (value ? ((this.getData() & 0x7) | 0x8) : (this.getData() & 0x7)));
	}

}