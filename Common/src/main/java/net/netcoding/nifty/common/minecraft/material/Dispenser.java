package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Dispenser extends DirectionalContainer {

	@Override
	Dispenser clone();

	@Override
	default BlockFace getFacing() {
		int data = this.getData() & 0x7;

		switch (data) {
			case 0x0:
				return BlockFace.DOWN;
			case 0x1:
				return BlockFace.UP;
			case 0x2:
				return BlockFace.NORTH;
			case 0x3:
				return BlockFace.SOUTH;
			case 0x4:
				return BlockFace.WEST;
			case 0x5:
			default:
				return BlockFace.EAST;
		}
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
			case DOWN:
				data = 0x0;
				break;
			case UP:
				data = 0x1;
				break;
			case NORTH:
				data = 0x2;
				break;
			case SOUTH:
				data = 0x3;
				break;
			case WEST:
				data = 0x4;
				break;
			case EAST:
			default:
				data = 0x5;
		}

		this.setData(data);
	}

}