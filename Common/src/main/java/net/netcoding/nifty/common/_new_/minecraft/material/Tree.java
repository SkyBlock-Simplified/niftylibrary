package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Tree extends Wood {

	@Override
	Tree clone();

	default BlockFace getDirection() {
		int data = ((this.getData() >> 2) & 0x3);

		switch (data) {
			case 0: // Up-down
			default:
				return BlockFace.UP;
			case 1: // North-south
				return BlockFace.WEST;
			case 2: // East-west
				return BlockFace.NORTH;
			case 3: // Directionless (all bark)
				return BlockFace.SELF;
		}
	}

	default void setDirection(BlockFace face) {
		int data;

		switch (face) {
			case UP:
			case DOWN:
			default:
				data = 0;
				break;
			case WEST:
			case EAST:
				data = 4; // 1<<2
				break;
			case NORTH:
			case SOUTH:
				data = 8; // 2<<2
				break;
			case SELF:
				data = 12; // 3<<2
				break;
		}

		this.setData((byte) ((this.getData() & 0x3) | data));
	}

}