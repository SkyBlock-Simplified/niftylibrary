package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

interface PistonMaterial extends MaterialData, Directional {

	@Override
	PistonMaterial clone();

	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x7);

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
				return BlockFace.EAST;
			default:
				return BlockFace.SELF;
		}
	}

	boolean isSticky();

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte)(this.getData() & 0x8);

		switch (face) {
			case UP:
				data |= 0x1;
				break;
			case NORTH:
				data |= 0x2;
				break;
			case SOUTH:
				data |= 0x3;
				break;
			case WEST:
				data |= 0x4;
				break;
			case EAST:
				data |= 0x5;
				break;
		}

		this.setData(data);
	}

}