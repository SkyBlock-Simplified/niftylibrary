package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface TripwireHook extends SimpleAttachableMaterialData, Redstone {

	@Override
	TripwireHook clone();

	@Override
	default BlockFace getAttachedFace() {
		byte data = (byte)(this.getData() & 0x3);

		switch (data) {
			case 0:
				return BlockFace.NORTH;
			case 1:
				return BlockFace.EAST;
			case 2:
				return BlockFace.SOUTH;
			case 3:
				return BlockFace.WEST;
		}

		return null;
	}

	default boolean isActivated() {
		return (this.getData() & 0x8) != 0;
	}

	default boolean isConnected() {
		return (this.getData() & 0x4) != 0;
	}

	@Override
	default boolean isPowered() {
		return this.isActivated();
	}

	default void setActivated(boolean value) {
		int data = (this.getData() & (0x4 | 0x3));

		if (value)
			data |= 0x8;

		this.setData((byte)data);
	}

	default void setConnected(boolean value) {
		int data = (this.getData() & (0x8 | 0x3));

		if (value)
			data |= 0x4;

		this.setData((byte)data);
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int data = (this.getData() & 0xC);

		switch (face) {
			case WEST:
				data |= 0x1;
				break;
			case NORTH:
				data |= 0x2;
				break;
			case EAST:
				data |= 0x3;
				break;
		}

		this.setData((byte)data);
	}

}