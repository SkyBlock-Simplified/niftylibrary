package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Torch extends SimpleAttachableMaterialData {

	@Override
	Torch clone();

	@Override
	default BlockFace getAttachedFace() {
		switch (this.getData()) {
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.EAST;
			case 0x3:
				return BlockFace.NORTH;
			case 0x4:
				return BlockFace.SOUTH;
			case 0x5:
			default:
				return BlockFace.DOWN;
		}
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
			case EAST:
				data = 0x1;
				break;
			case WEST:
				data = 0x2;
				break;
			case SOUTH:
				data = 0x3;
				break;
			case NORTH:
				data = 0x4;
				break;
			case UP:
			default:
				data = 0x5;
		}

		this.setData(data);
	}
}