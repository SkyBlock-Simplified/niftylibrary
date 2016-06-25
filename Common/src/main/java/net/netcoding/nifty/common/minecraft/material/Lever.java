package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Lever extends SimpleAttachableMaterialData, OptionalRedstone {

	@Override
	Lever clone();

	@Override
	default BlockFace getAttachedFace() {
		byte data = (byte) (this.getData() & 0x7);

		switch (data) {
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.EAST;
			case 0x3:
				return BlockFace.NORTH;
			case 0x4:
				return BlockFace.SOUTH;
			case 0x5:
			case 0x6:
				return BlockFace.DOWN;
			case 0x0:
			case 0x7:
				return BlockFace.UP;
		}

		return null;
	}

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x8) == 0x8;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte) (this.getData() & 0x8);
		BlockFace attach = this.getAttachedFace();

		if (attach == BlockFace.DOWN) {
			switch (face) {
				case SOUTH:
				case NORTH:
					data |= 0x5;
					break;
				case EAST:
				case WEST:
					data |= 0x6;
					break;
			}
		} else if (attach == BlockFace.UP) {
			switch (face) {
				case SOUTH:
				case NORTH:
					data |= 0x7;
					break;
				case EAST:
				case WEST:
					data |= 0x0;
					break;
			}
		} else {
			switch (face) {
				case EAST:
					data |= 0x1;
					break;
				case WEST:
					data |= 0x2;
					break;
				case SOUTH:
					data |= 0x3;
					break;
				case NORTH:
					data |= 0x4;
					break;
			}
		}

		this.setData(data);
	}

	@Override
	default void setPowered(boolean isPowered) {
		this.setData((byte) (isPowered ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}