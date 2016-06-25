package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

/**
 * Represents a material that can contain other materials.
 */
interface DirectionalContainer extends MaterialData, Directional {

	@Override
	DirectionalContainer clone();

	@Override
	default BlockFace getFacing() {
		switch (this.getData()) {
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