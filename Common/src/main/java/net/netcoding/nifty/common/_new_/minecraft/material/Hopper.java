package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Hopper extends MaterialData, Directional, Redstone {

	@Override
	Hopper clone();

	@Override
	default BlockFace getFacing() {
		byte data = (byte) (this.getData() & 0x7);

		switch (data) {
			default:
			case 0x0:
				return BlockFace.DOWN;
			case 0x2:
				return BlockFace.NORTH;
			case 0x3:
				return BlockFace.SOUTH;
			case 0x4:
				return BlockFace.WEST;
			case 0x5:
				return BlockFace.EAST;
		}
	}

	default boolean isActive() {
		return (this.getData() & 0x8) == 0;
	}

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x8) != 0;
	}

	default void setActive(boolean value) {
		this.setData((byte)(this.getData() & 0x7 | (value ? 0x0 : 0x8)));
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int data = (this.getData() & 0x8);

		switch (face) {
			case DOWN:
				data |= 0x0;
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

		this.setData((byte)data);
	}

}