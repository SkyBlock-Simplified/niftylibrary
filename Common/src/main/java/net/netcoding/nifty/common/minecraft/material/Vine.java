package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Vine extends MaterialData {

	@Override
	Vine clone();

	/**
	 * Check if the vine is attached to the specified face of an adjacent
	 * block. You can check two faces at once by passing e.g. {@link
	 * BlockFace#NORTH_EAST}.
	 *
	 * @param face The face to check.
	 * @return Whether it is attached to that face.
	 */
	default boolean isOnFace(BlockFace face) {
		switch (face) {
			case WEST:
				return (this.getData() & 0x2) == 0x2;
			case NORTH:
				return (this.getData() & 0x4) == 0x4;
			case SOUTH:
				return (this.getData() & 0x1) == 0x1;
			case EAST:
				return (this.getData() & 0x8) == 0x8;
			case NORTH_EAST:
				return isOnFace(BlockFace.EAST) && isOnFace(BlockFace.NORTH);
			case NORTH_WEST:
				return isOnFace(BlockFace.WEST) && isOnFace(BlockFace.NORTH);
			case SOUTH_EAST:
				return isOnFace(BlockFace.EAST) && isOnFace(BlockFace.SOUTH);
			case SOUTH_WEST:
				return isOnFace(BlockFace.WEST) && isOnFace(BlockFace.SOUTH);
			case UP: // It's impossible to be accurate with this since it's contextual
				return true;
			default:
				return false;
		}
	}

	/**
	 * Attach the vine to the specified face of an adjacent block.
	 *
	 * @param face The face to attach.
	 */
	default void putOnFace(BlockFace face) {
		switch(face) {
			case WEST:
				this.setData((byte)(this.getData() | 0x2)); // VINE_WEST
				break;
			case NORTH:
				this.setData((byte)(this.getData() | 0x4)); // VINE_NORTH
				break;
			case SOUTH:
				this.setData((byte)(this.getData() | 0x1)); // VINE_SOUTH
				break;
			case EAST:
				this.setData((byte)(this.getData() | 0x8)); // VINE_EAST
				break;
			case NORTH_WEST:
				putOnFace(BlockFace.WEST);
				putOnFace(BlockFace.NORTH);
				break;
			case SOUTH_WEST:
				putOnFace(BlockFace.WEST);
				putOnFace(BlockFace.SOUTH);
				break;
			case NORTH_EAST:
				putOnFace(BlockFace.EAST);
				putOnFace(BlockFace.NORTH);
				break;
			case SOUTH_EAST:
				putOnFace(BlockFace.EAST);
				putOnFace(BlockFace.SOUTH);
				break;
			case UP:
				break;
			default:
				throw new IllegalArgumentException("Vines can't go on face " + face.toString());
		}
	}

	/**
	 * Detach the vine from the specified face of an adjacent block.
	 *
	 * @param face The face to detach.
	 */
	default void removeFromFace(BlockFace face) {
		switch(face) {
			case WEST:
				this.setData((byte)(this.getData() & ~0x2));
				break;
			case NORTH:
				this.setData((byte)(this.getData() & ~0x4));
				break;
			case SOUTH:
				this.setData((byte)(this.getData() & ~0x1));
				break;
			case EAST:
				this.setData((byte)(this.getData() & ~0x8));
				break;
			case NORTH_WEST:
				removeFromFace(BlockFace.WEST);
				removeFromFace(BlockFace.NORTH);
				break;
			case SOUTH_WEST:
				removeFromFace(BlockFace.WEST);
				removeFromFace(BlockFace.SOUTH);
				break;
			case NORTH_EAST:
				removeFromFace(BlockFace.EAST);
				removeFromFace(BlockFace.NORTH);
				break;
			case SOUTH_EAST:
				removeFromFace(BlockFace.EAST);
				removeFromFace(BlockFace.SOUTH);
				break;
			case UP:
				break;
			default:
				throw new IllegalArgumentException("Vines can't go on face " + face.toString());
		}
	}

}