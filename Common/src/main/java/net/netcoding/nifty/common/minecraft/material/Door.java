package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.TreeSpecies;
import net.netcoding.nifty.common.minecraft.block.BlockFace;

public interface Door extends MaterialData, Directional, Openable {

	@Override
	Door clone();

	/**
	 * Get the direction that this door is facing.
	 *
	 * Undefined if {@link #isTopHalf()} is true.
	 *
	 * @return the direction
	 */
	@Override
	default BlockFace getFacing() {
		byte data = (byte)(this.getData() & 0x3);

		switch (data) {
			case 0:
				return BlockFace.WEST;
			case 1:
				return BlockFace.NORTH;
			case 2:
				return BlockFace.EAST;
			case 3:
				return BlockFace.SOUTH;
			default:
				throw new IllegalStateException("Unknown door facing (data: " + data + ")");
		}
	}

	/**
	 * Returns the side of the door the hinge is on.
	 *
	 * Undefined if {@link #isTopHalf()} is false.
	 *
	 * @return false for left hinge, true for right hinge
	 */
	default boolean getHinge() {
		return (this.getData() & 0x1) == 1;
	}

	static Material getWoodDoorOfSpecies(TreeSpecies species) {
		switch (species) {
			default:
			case GENERIC:
				return Material.WOODEN_DOOR;
			case BIRCH:
				return Material.BIRCH_DOOR;
			case REDWOOD:
				return Material.SPRUCE_DOOR;
			case JUNGLE:
				return Material.JUNGLE_DOOR;
			case ACACIA:
				return Material.ACACIA_DOOR;
			case DARK_OAK:
				return Material.DARK_OAK_DOOR;
		}
	}

	/**
	 * @return whether this is the top half of the door
	 */
	default boolean isTopHalf() {
		return (this.getData() & 0x8) == 0x8;
	}

	/**
	 * Result is undefined if <code>isTopHalf()</code> is true.
	 */
	@Override
	default boolean isOpen() {
		return (this.getData() & 0x4) == 0x4;
	}

	/**
	 * Set the direction that this door should is facing.
	 *
	 * Undefined if {@link #isTopHalf()} is true.
	 *
	 * @param face the direction
	 */
	@Override
	default void setFacingDirection(BlockFace face) {
		byte data = (byte)(this.getData() & 0xC);

		switch (face) {
			case WEST:
				data |= 0x0;
				break;
			case NORTH:
				data |= 0x1;
				break;
			case EAST:
				data |= 0x2;
				break;
			case SOUTH:
				data |= 0x3;
				break;
		}

		this.setData(data);
	}

	/**
	 * Set whether the hinge is on the left or right side. Left is false, right is true.
	 *
	 * Undefined if {@link #isTopHalf()} is false.
	 *
	 * @param isHingeRight True if the hinge is on the right hand side, false if the hinge is on the left hand side.
	 */
	default void setHinge(boolean isHingeRight) {
		this.setData((byte) (isHingeRight ? (this.getData() | 0x1) : (this.getData() & ~0x1)));
	}

	/**
	 * Set whether the door is open. Undefined if <code>isTopHalf()</code> is true.
	 */
	@Override
	default void setOpen(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x4) : (this.getData() & ~0x4)));
	}

	/**
	 * Configure this part of the door to be either the top or the bottom half
	 *
	 * @param value True to make it the top half.
	 */
	default void setTopHalf(boolean value) {
		this.setData((byte)(value ? (this.getData() | 0x8) : (this.getData() & ~0x8)));
	}

}