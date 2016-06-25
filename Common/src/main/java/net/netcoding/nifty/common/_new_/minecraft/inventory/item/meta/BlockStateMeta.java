package net.netcoding.nifty.common._new_.minecraft.inventory.item.meta;

import net.netcoding.nifty.common._new_.minecraft.block.BlockState;

public interface BlockStateMeta extends ItemMeta {

	/**
	 * Returns the currently attached block state for this
	 * item or creates a new one if one doesn't exist.
	 *
	 * The state is a copy, it must be set back (or to another
	 * item) with {@link #setBlockState(BlockState)}
	 *
	 * @return the attached state or a new state
	 */
	BlockState getBlockState();

	/**
	 * Returns whether the item has a block state currently
	 * attached to it.
	 *
	 * @return whether a block state is already attached
	 */
	boolean hasBlockState();

	/**
	 * Attaches a copy of the passed block state to the item.
	 *
	 * @param blockState the block state to attach to the block.
	 * @throws IllegalArgumentException if the blockState is null
	 *         or invalid for this item.
	 */
	void setBlockState(BlockState blockState);

}