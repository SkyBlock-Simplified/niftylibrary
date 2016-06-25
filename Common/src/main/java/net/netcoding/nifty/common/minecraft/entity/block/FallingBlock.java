package net.netcoding.nifty.common.minecraft.entity.block;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.material.Material;

/**
 * Represents a Falling Block.
 */
public interface FallingBlock extends Entity {

	/**
	 * Gets if this falling block can hurt entities.
	 *
	 * @return True if entities will be damaged by this block.
	 */
	boolean canHurtEntities();

	/**
	 * Gets the ID of the falling block.
	 *
	 * @return ID type of the block.
	 */
	default int getBlockId() {
		return this.getMaterial().getId();
	}

	/**
	 * Gets the data for the falling block.
	 *
	 * @return Data of the block.
	 */
	@Deprecated
	byte getBlockData();

	/**
	 * Gets the Material of the falling block.
	 *
	 * @return Material of the block.
	 */
	Material getMaterial();

	/**
	 * Set if the falling block will break into an item if it cannot be placed.
	 *
	 * @param dropItem True to break into an item when obstructed.
	 */
	void setDropItem(boolean dropItem);

	/**
	 * Set the HurtEntities state of this block.
	 *
	 * @param hurtEntities True if entities should be damaged by this block.
	 */
	void setHurtEntities(boolean hurtEntities);

	/**
	 * Get if the falling block will break into an item if it cannot be placed.
	 *
	 * @return True if the block will break into an item when obstructed.
	 */
	boolean willDropItem();

}