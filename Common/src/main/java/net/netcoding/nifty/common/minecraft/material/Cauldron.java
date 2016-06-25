package net.netcoding.nifty.common.minecraft.material;

public interface Cauldron extends MaterialData {

	@Override
	Cauldron clone();

	default boolean isFull() {
		return this.getData() >= 3;
	}

	default boolean isEmpty() {
		return this.getData() <= 0;
	}

}