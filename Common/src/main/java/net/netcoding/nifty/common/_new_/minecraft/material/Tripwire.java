package net.netcoding.nifty.common._new_.minecraft.material;

public interface Tripwire extends MaterialData {

	@Override
	Tripwire clone();

	default boolean isActivated() {
		return (this.getData() & 0x4) != 0;
	}

	default boolean isObjectTriggering() {
		return (this.getData() & 0x1) != 0;
	}

	default void setActivated(boolean value) {
		int data = this.getData() & (0x8 | 0x3);

		if (value)
			data |= 0x4;

		setData((byte)data);
	}

	default void setObjectTriggering(boolean value) {
		int data = this.getData() & 0xE;

		if (value)
			data |= 0x1;

		setData((byte)data);
	}

}