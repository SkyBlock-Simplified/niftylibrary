package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Button extends SimpleAttachableMaterialData, OptionalRedstone {

	@Override
	Button clone();

	@Override
	default BlockFace getAttachedFace() {
		byte data = (byte)(this.getData() & 0x7);

		switch (data) {
			case 0x0:
				return BlockFace.UP;
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.EAST;
			case 0x3:
				return BlockFace.NORTH;
			case 0x4:
				return BlockFace.SOUTH;
			case 0x5:
				return BlockFace.DOWN;
		}

		return null;
	}

	@Override
	default boolean isPowered() {
		return (this.getData() & 0x8) == 0x8;
	}

	default void setFacingDirection(BlockFace face) {
		byte data = (byte) (this.getData() & 0x8);

		switch (face) {
			case DOWN:
				data |= 0x0;
				break;
			case EAST:
				data |= 0x1;
				break;
			case WEST:
				data |= 0x2;
				break;
			case SOUTH:
				data |= 0x3;
				break;
			case NORTH:
				data |= 0x4;
				break;
			case UP:
				data |= 0x5;
				break;
		}

		this.setData(data);
	}

	@Override
	default void setPowered(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}