package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.BlockProjectileSource;

public interface Dispenser extends BlockState, InventoryHolder {

	/**
	 * Gets the BlockProjectileSource object for this dispenser.
	 * <p>
	 * If the block is no longer a dispenser, this will return null.
	 *
	 * @return A BlockProjectileSource if valid, otherwise null.
	 */
	BlockProjectileSource getBlockProjectileSource();

	/**
	 * Attempts to dispense the contents of this block.
	 * <p>
	 * If the block is no longer a dispenser, this will return false.
	 *
	 * @return True if successful.
	 */
	boolean dispense();

}