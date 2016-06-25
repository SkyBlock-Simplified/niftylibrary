package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.common.minecraft.block.BlockFace;

/**
 * Indicates that a block can be attached to another block.
 */
public interface Attachable extends Directional {

	/**
	 * Gets the face that this block is attached to.
	 *
	 * @return BlockFace attached to.
	 */
	BlockFace getAttachedFace();

}