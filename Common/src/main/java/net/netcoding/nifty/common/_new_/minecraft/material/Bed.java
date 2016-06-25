package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Bed extends MaterialData, Directional {

	@Override
	Bed clone();

	/**
	 * Get the direction that this bed's head is facing toward
	 *
	 * @return the direction the head of the bed is facing
	 */
	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x7);

		switch (data) {
			case 0x0:
				return BlockFace.SOUTH;
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.NORTH;
			case 0x3:
			default:
				return BlockFace.EAST;
		}
	}

	default boolean isHeadOfBed() {
		return (this.getData() & 0x8) == 0x8;
	}

	/**
	 * Set which direction the head of the bed is facing. Note that this will
	 * only affect one of the two blocks the bed is made of.
	 */
	@Override
	default void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
			case SOUTH:
				data = 0x0;
				break;
			case WEST:
				data = 0x1;
				break;
			case NORTH:
				data = 0x2;
				break;
			case EAST:
			default:
				data = 0x3;
		}

		if (this.isHeadOfBed())
			data |= 0x8;

		this.setData(data);
	}


	default void setHeadOfBed(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}