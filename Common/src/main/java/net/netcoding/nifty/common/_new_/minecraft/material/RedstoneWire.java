package net.netcoding.nifty.common._new_.minecraft.material;

public interface RedstoneWire extends MaterialData, Redstone {

	@Override
	RedstoneWire clone();

	@Override
	default boolean isPowered() {
		return this.getData() > 0;
	}

}