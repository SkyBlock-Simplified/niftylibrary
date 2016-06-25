package net.netcoding.nifty.common.minecraft.material;

public interface WoodenStep extends Wood, Invertable {

	@Override
	WoodenStep clone();

	/**
	 * Test if step is inverted.
	 *
	 * @return True if inverted (top half), false if normal (bottom half).
	 */
	@Override
	default boolean isInverted() {
		return ((getData() & 0x8) != 0);
	}

	/**
	 * Set step inverted state.
	 *
	 * @param value - True if step is inverted (top half), false if step is normal (bottom half).
	 */
	@Override
	default void setInverted(boolean value) {
		int data = (this.getData() & 0x7);

		if (value)
			data |= 0x8;

		this.setData((byte)data);
	}

}