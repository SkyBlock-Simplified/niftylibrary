package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

public interface CocoaPlant extends SimpleAttachableMaterialData {

	@Override
	CocoaPlant clone();

	@Override
	default BlockFace getAttachedFace() {
		return getFacing().getOppositeFace();
	}

	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x3);

		switch (data) {
			case 0x0:
				return BlockFace.SOUTH;
			case 0x1:
				return BlockFace.WEST;
			case 0x2:
				return BlockFace.NORTH;
			case 0x3:
				return BlockFace.EAST;
		}

		return null;
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int dat = (this.getData() & 0xC);

		switch (face) {
			default:
			case SOUTH:
				break;
			case WEST:
				dat |= 0x1;
				break;
			case NORTH:
				dat |= 0x2;
				break;
			case EAST:
				dat |= 0x3;
				break;
		}
		setData((byte) dat);
	}

	default Size getSize() {
		byte data = (byte)(this.getData() & 0xC);

		switch (data) {
			case 0x0:
				return Size.SMALL;
			case 0x4:
				return Size.MEDIUM;
			default:
				return Size.LARGE;
		}
	}

	default void setSize(Size size) {
		int data = getData() & 0x3;

		switch (size) {
			case SMALL:
				break;
			case MEDIUM:
				data |= 0x4;
				break;
			case LARGE:
				data |= 0x8;
				break;
		}
		this.setData((byte)data);
	}

	enum Size {

		SMALL,
		MEDIUM,
		LARGE

	}

}