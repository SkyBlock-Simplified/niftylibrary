package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.types.BrewerInventory;

public interface BrewingStand extends BlockState, InventoryHolder {

	/**
	 * How much time is left in the brewing cycle
	 *
	 * @return Brew Time
	 */
	int getBrewingTime();

	/**
	 * Get the level of current fuel for brewing.
	 *
	 * @return The fuel level
	 */
	int getFuelLevel();

	@Override
	BrewerInventory getInventory();

	/**
	 * Set the time left before brewing completes.
	 *
	 * @param brewTime Brewing time
	 */
	void setBrewingTime(int brewTime);

	/**
	 * Set the level of current fuel for brewing.
	 *
	 * @param level fuel level
	 */
	void setFuelLevel(int level);

}