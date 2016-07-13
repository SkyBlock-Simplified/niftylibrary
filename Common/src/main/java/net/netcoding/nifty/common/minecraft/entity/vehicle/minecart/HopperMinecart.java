package net.netcoding.nifty.common.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;

/**
 * Represents a Minecart with a Hopper inside it.
 */
public interface HopperMinecart extends Minecart, InventoryHolder {

	/**
	 * Checks whether or not this Minecart will pick up items into its inventory.
	 *
	 * @return True if the Minecart will pick up items.
	 */
	boolean isEnabled();

	/**
	 * Sets whether this Minecart will pick up items.
	 *
	 * @param value True to allow item pickup.
	 */
	void setEnabled(boolean value);

}