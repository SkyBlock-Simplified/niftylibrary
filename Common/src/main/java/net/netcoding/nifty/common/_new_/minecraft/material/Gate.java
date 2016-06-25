package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Gate extends MaterialData, Directional, Openable {

	// OPEN_BIT = 0x4
	// DIR_BIT = 0x3;

	@Override
	Gate clone();

	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x3);

		switch (data) {
			case 0x0: // GATE_SOUTH
				return BlockFace.EAST;
			case 0x1: // GATE_WEST
				return BlockFace.SOUTH;
			case 0x2: // GATE_NORTH
				return BlockFace.WEST;
			case 0x3: // GATE_EAST
				return BlockFace.NORTH;
		}

		return BlockFace.EAST;
	}

	@Override
	default boolean isOpen() {
		return (getData() & 0x4) > 0;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte)(this.getData() &~ 0x3);

		switch (face) {
			default:
			case EAST:
				data |= 0x0; // GATE_SOUTH
				break;
			case SOUTH:
				data |= 0x1; // GATE_WEST
				break;
			case WEST:
				data |= 0x2; // GATE_NORTH
				break;
			case NORTH:
				data |= 0x3; // GATE_EAST
				break;
		}

		this.setData(data);
	}

	@Override
	default void setOpen(boolean value) {
		byte data = this.getData();

		if (value)
			data |= 0x4;
		else
			data &= ~0x4;

		this.setData(data);
	}

}