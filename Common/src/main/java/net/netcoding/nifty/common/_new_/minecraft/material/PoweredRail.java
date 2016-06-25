package net.netcoding.nifty.common._new_.minecraft.material;

public interface PoweredRail extends ExtendedRails, OptionalRedstone {

	@Override
	PoweredRail clone();

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x8) == 0x8;
	}

	@Override
	default void setPowered(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}