package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Pumpkin extends MaterialData, Directional {

	@Override
	Pumpkin clone();

	@Override
	default BlockFace getFacing() {
		byte data = this.getData();

		switch (data) {
			case 0x0:
				return BlockFace.NORTH;
			case 0x1:
				return BlockFace.EAST;
			case 0x2:
				return BlockFace.SOUTH;
			case 0x3:
			default:
				return BlockFace.EAST;
		}
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
			case NORTH:
				data = 0x0;
				break;
			case EAST:
				data = 0x1;
				break;
			case SOUTH:
				data = 0x2;
				break;
			case WEST:
			default:
				data = 0x3;
		}

		this.setData(data);
	}

}