package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface ExtendedRails extends Rails {

	@Override
	ExtendedRails clone();

	@Override
	default byte getConvertedData() {
		return (byte)(this.getData() & 0x7);
	}

	@Override
	default boolean isCurve() {
		return false;
	}

	@Override
	default void setDirection(BlockFace face, boolean sloped) {
		boolean extraBitSet = (this.getData() & 0x8) == 0x8;

		if (face != BlockFace.WEST && face != BlockFace.EAST && face != BlockFace.NORTH && face != BlockFace.SOUTH)
			throw new IllegalArgumentException("Detector rails and powered rails cannot be set on a curve!");

		Rails.super.setDirection(face, sloped);
		this.setData((byte) (extraBitSet ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}