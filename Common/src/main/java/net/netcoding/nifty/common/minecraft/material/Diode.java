package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Diode extends MaterialData, Directional, Redstone {

	@Override
	Diode clone();

	default int getDelay() {
		return (this.getData() >> 2) + 1;
	}

	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x3);

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

	@Override
	default boolean isPowered() {
		return Material.DIODE_BLOCK_ON == this.getItemType();
	}

	default void setDelay(int delay) {
		delay = Math.min(delay, 4);
		delay = Math.max(delay, 1);
		byte newData = (byte) (this.getData() & 0x3);
		this.setData((byte) (newData | ((delay - 1) << 2)));
	}

	@Override
	default void setFacingDirection(BlockFace face) {
		int delay = this.getDelay();
		byte data;

		switch (face) {
			case EAST:
				data = 0x1;
				break;
			case SOUTH:
				data = 0x2;
				break;
			case WEST:
				data = 0x3;
				break;
			case NORTH:
			default:
				data = 0x0;
		}

		this.setData(data);
		this.setDelay(delay);
	}

}