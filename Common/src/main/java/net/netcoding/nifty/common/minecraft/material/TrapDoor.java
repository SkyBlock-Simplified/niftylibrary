package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface TrapDoor extends SimpleAttachableMaterialData, Invertable, Openable {

	@Override
	TrapDoor clone();

	@Override
	default BlockFace getAttachedFace() {
		byte data = (byte) (this.getData() & 0x3);

		switch (data) {
			case 0x0:
				return BlockFace.SOUTH;
			case 0x1:
				return BlockFace.NORTH;
			case 0x2:
				return BlockFace.EAST;
			case 0x3:
				return BlockFace.WEST;
		}

		return null;

	}

	/**
	 * Test if trapdoor is inverted.
	 *
	 * @return True if inverted (top half), false if normal (bottom half)
	 */
	@Override
	default boolean isInverted() {
		return (this.getData() & 0x8) != 0;
	}

	@Override
	default boolean isOpen() {
		return (this.getData() & 0x4) == 0x4;
	}

	/**
	 * Set trapdoor inverted state.
	 *
	 * @param value - True if inverted (top half), false if normal (bottom half)
	 */
	@Override
	default void setInverted(boolean value) {
		int data = (this.getData() & 0x7);

		if (value)
			data |= 0x8;

		this.setData((byte) data);
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte)(this.getData() & 0xC);

		switch (face) {
			case SOUTH:
				data |= 0x1;
				break;
			case WEST:
				data |= 0x2;
				break;
			case EAST:
				data |= 0x3;
				break;
		}

		this.setData(data);
	}

	@Override
	default void setOpen(boolean isOpen) {
		byte data = this.getData();

		if (isOpen) {
			data |= 0x4;
		} else {
			data &= ~0x4;
		}

		this.setData(data);
	}

}