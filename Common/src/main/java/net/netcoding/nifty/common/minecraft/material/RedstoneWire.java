package net.netcoding.nifty.common.minecraft.material;

public interface RedstoneWire extends MaterialData, Redstone {

	@Override
	RedstoneWire clone();

	@Override
	default boolean isPowered() {
		return this.getData() > 0;
	}

}