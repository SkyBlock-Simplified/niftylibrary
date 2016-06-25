package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Skull extends MaterialData, Directional {

	@Override
	Skull clone();

	@Override
	default BlockFace getFacing() {
		switch (this.getData()) {
			case 0x1:
			default:
				return BlockFace.SELF;
			case 0x2:
				return BlockFace.NORTH;
			case 0x3:
				return BlockFace.SOUTH;
			case 0x4:
				return BlockFace.EAST;
			case 0x5:
				return BlockFace.WEST;
		}
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int data;

		switch (face) {
			case SELF:
			default:
				data = 0x1;
				break;
			case NORTH:
				data = 0x2;
				break;
			case EAST:
				data = 0x4;
				break;
			case SOUTH:
				data = 0x3;
				break;
			case WEST:
				data = 0x5;
		}

		setData((byte)data);
	}


}