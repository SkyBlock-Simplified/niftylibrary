package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface Comparator extends MaterialData, Directional, Redstone {

	@Override
	Comparator clone();

	@Override
	default BlockFace getFacing() {
		byte data = (byte)(getData() & 0x3);

		switch (data) {
			case 0x0:
			default:
				return BlockFace.NORTH;
			case 0x1:
				return BlockFace.EAST;
			case 0x2:
				return BlockFace.SOUTH;
			case 0x3:
				return BlockFace.WEST;
		}
	}

	default boolean isBeingPowered() {
		return (this.getData() & 0x8) != 0;
	}

	@Override
	default boolean isPowered() {
		return Material.REDSTONE_COMPARATOR_ON == this.getItemType();
	}

	default boolean isSubtractionMode() {
		return (this.getData() & 0x4) != 0;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int data = (getData() & 0xC);

		switch (face) {
			case EAST:
				data |= 0x1;
				break;
			case SOUTH:
				data |= 0x2;
				break;
			case WEST:
				data |= 0x3;
				break;
			case NORTH:
			default:
				data |= 0x0;
		}

		this.setData((byte)data);
	}

	default void setSubtractionMode(boolean value) {
		this.setData((byte)(this.getData() & 0xB | (value ? 0x4 : 0x0)));
	}

}