package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Ladder extends SimpleAttachableMaterialData {

	@Override
	Ladder clone();

	@Override
	default BlockFace getAttachedFace() {
		switch (this.getData()) {
			case 0x2:
				return BlockFace.SOUTH;
			case 0x3:
				return BlockFace.NORTH;
			case 0x4:
				return BlockFace.EAST;
			case 0x5:
				return BlockFace.WEST;
		}

		return null;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte)0x0;

		switch (face) {
			case SOUTH:
				data = 0x2;
				break;
			case NORTH:
				data = 0x3;
				break;
			case EAST:
				data = 0x4;
				break;
			case WEST:
				data = 0x5;
				break;
		}

		this.setData(data);
	}

}