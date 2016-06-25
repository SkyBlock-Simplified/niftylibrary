package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Rails extends MaterialData {

	@Override
	Rails clone();

	default byte getConvertedData() {
		return this.getData();
	}

	default BlockFace getDirection() {
		byte data = this.getConvertedData();

		switch (data) {
			case 0x0:
			default:
				return BlockFace.SOUTH;
			case 0x1:
				return BlockFace.EAST;
			case 0x2:
				return BlockFace.EAST;
			case 0x3:
				return BlockFace.WEST;
			case 0x4:
				return BlockFace.NORTH;
			case 0x5:
				return BlockFace.SOUTH;
			case 0x6:
				return BlockFace.NORTH_WEST;
			case 0x7:
				return BlockFace.NORTH_EAST;
			case 0x8:
				return BlockFace.SOUTH_EAST;
			case 0x9:
				return BlockFace.SOUTH_WEST;
		}
	}

	default boolean isOnSlope() {
		byte data = this.getConvertedData();
		return (data >= 0x2 && data <= 0x5);
	}

	default boolean isCurve() {
		byte data = this.getConvertedData();
		return (data >= 0x6 && data <= 0x9);
	}

	default void setDirection(BlockFace face, boolean sloped) {
		int data;

		switch (face) {
			case EAST:
			default:
				data = (sloped ? 0x2 : 0x1);
				break;
			case WEST:
				data = (sloped ? 0x3 : 0x1);
				break;
			case NORTH:
				data = (sloped ? 0x4 : 0x0);
				break;
			case SOUTH:
				data = (sloped ? 0x5 : 0x0);
				break;
			case NORTH_WEST:
				data = 0x6;
				break;
			case NORTH_EAST:
				data = 0x7;
				break;
			case SOUTH_EAST:
				data = 0x8;
				break;
			case SOUTH_WEST:
				data = 0x9;
				break;
		}

		this.setData((byte)data);
	}

}