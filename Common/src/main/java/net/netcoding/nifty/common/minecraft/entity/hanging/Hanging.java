package net.netcoding.nifty.common.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.block.BlockFace;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.material.Attachable;

/**
 * Represents a Hanging Entity.
 */
public interface Hanging extends Entity, Attachable {

	/**
	 * Sets the direction of the hanging entity, potentially overriding rules
	 * of placement. Note that if the result is not valid the object would
	 * normally drop as an item.
	 *
	 * @param face The new direction.
	 * @param force Whether to force it.
	 * @return False if force was false and there was no block for it to attach to.
	 */
	boolean setFacingDirection(BlockFace face, boolean force);

}