package net.netcoding.nifty.common.minecraft.material;

public interface DetectorRail extends ExtendedRails, OptionalPressureSensor {

	@Override
	DetectorRail clone();

	@Override
	default boolean isPressed() {
		return (this.getData() & 0x8) == 0x8;
	}

	@Override
	default void setPressed(boolean value) {
		this.setData((byte) (value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}