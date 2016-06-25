package net.netcoding.nifty.common.minecraft.material;

public interface PressurePlate extends MaterialData, PressureSensor {

	@Override
	PressurePlate clone();

	@Override
	default boolean isPressed() {
		return this.getData() == 0x1;
	}

}