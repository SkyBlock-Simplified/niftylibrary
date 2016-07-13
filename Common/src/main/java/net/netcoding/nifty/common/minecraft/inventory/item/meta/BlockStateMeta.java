package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.block.state.BlockState;

public interface BlockStateMeta extends ItemMeta {

	@Override
	BlockStateMeta clone();

	/**
	 * Gets if the currently attached block state for this item or creates a new one if one doesn't exist.
	 * <p>
	 * The state is a copy, it must be set back (or to another item) with {@link #setBlockState(BlockState)}
	 *
	 * @return The attached state or a new state.
	 */
	BlockState getBlockState();

	/**
	 * Checks if the item has a block state currently attached to it.
	 *
	 * @return Whether a block state is already attached.
	 */
	boolean hasBlockState();

	/**
	 * Attaches a copy of the passed block state to the item.
	 *
	 * @param state The block state to attach to the block.
	 * @throws IllegalArgumentException If the blockState is null or invalid for this item.
	 */
	void setBlockState(BlockState state);

}