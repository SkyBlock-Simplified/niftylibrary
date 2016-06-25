package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Stairs extends MaterialData, Directional, Invertable {

	@Override
	Stairs clone();

	default BlockFace getAscendingDirection() {
		byte data = (byte)(this.getData() & 0x3);

		switch (data) {
			case 0x0:
			default:
				return BlockFace.EAST;
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.SOUTH;
			case 0x3:
				return BlockFace.NORTH;
		}
	}

	default BlockFace getDescendingDirection() {
		return this.getAscendingDirection().getOppositeFace();
	}

	@Override
	default BlockFace getFacing() {
		return this.getDescendingDirection();
	}

	@Override
	default boolean isInverted() {
		return (this.getData() & 0x4) != 0;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
			case NORTH:
				data = 0x3;
				break;
			case SOUTH:
				data = 0x2;
				break;
			case EAST:
			default:
				data = 0x0;
				break;
			case WEST:
				data = 0x1;
				break;
		}

		this.setData((byte)((this.getData() & 0xC) | data));
	}

	@Override
	default void setInverted(boolean value) {
		int data = (this.getData() & 0x3);

		if (value)
			data |= 0x4;

		this.setData((byte)data);
	}

}