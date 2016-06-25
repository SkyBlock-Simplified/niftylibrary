package net.netcoding.nifty.common._new_.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common._new_.minecraft.inventory.InventoryHolder;

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
	 * @param enabled True to allow item pickup.
	 */
	void setEnabled(boolean enabled);

}