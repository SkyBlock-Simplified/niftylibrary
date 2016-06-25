package net.netcoding.nifty.common.minecraft.entity.living.animal;

import net.netcoding.nifty.common.minecraft.entity.vehicle.Vehicle;

/**
 * Represents a Pig.
 */
public interface Pig extends Animal, Vehicle {

	/**
	 * Check if the pig has a saddle.
	 *
	 * @return True if the pig has been saddled.
	 */
	boolean hasSaddle();

	/**
	 * Sets if the pig has a saddle or not
	 *
	 * @param saddled True if the pig has a saddle or not.
	 */
	void setSaddle(boolean saddled);

}