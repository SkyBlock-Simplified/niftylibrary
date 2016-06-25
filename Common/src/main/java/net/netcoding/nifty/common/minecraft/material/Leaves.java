package net.netcoding.nifty.common.minecraft.material;

public interface Leaves extends Wood {

	@Override
	Leaves clone();

	default boolean isDecayable() {
		return (this.getData() & 0x4) == 0;
	}

	default boolean isDecaying() {
		return (this.getData() & 0x8) != 0;
	}

	default void setDecayable(boolean value) {
		this.setData((byte)((this.getData() & 0x3) | (value
				? (this.getData() & 0x8) // Only persist the decaying flag if this is a decayable block
				: 0x4)));
	}

	default void setDecaying(boolean value) {
		this.setData((byte) ((this.getData() & 0x3) | (value
				? 0x8 // Clear the permanent flag to make this a decayable flag and set the decaying flag
				: (this.getData() & 0x4)))); // Only persist the decayable flag if this is not a decaying block
	}

}