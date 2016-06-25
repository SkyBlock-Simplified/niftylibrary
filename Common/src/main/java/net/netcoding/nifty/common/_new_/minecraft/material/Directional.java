package net.netcoding.nifty.common._new_.minecraft.material;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;

/**
 * Indicates that a block can change its facing direction.
 */
public interface Directional {

	/**
	 * Gets the direction this block is facing.
	 *
	 * @return The direction this block is facing.
	 */
	BlockFace getFacing();

	/**
	 * Sets the direction that this block is facing.
	 *
	 * @param face The facing direction.
	 */
	void setFacingDirection(BlockFace face);

}