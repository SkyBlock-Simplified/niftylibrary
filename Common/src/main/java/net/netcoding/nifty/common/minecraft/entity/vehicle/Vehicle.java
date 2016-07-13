package net.netcoding.nifty.common.minecraft.entity.vehicle;

import net.netcoding.nifty.common.minecraft.entity.Entity;

/**
 * Represents a vehicle entity.
 */
public interface Vehicle extends Entity {

	/**
	 * Gets the primary passenger of this vehicle. If this vehicle can have
	 * multiple passengers, this will only return the primary passenger.
	 *
	 * @return The passenger, or null if no passengers.
	 */
	@Override
	Entity getPassenger();

	/**
	 * Check if this vehicle has passengers.
	 *
	 * @return True if this vehicle has no passengers.
	 */
	@Override
	boolean isEmpty();

	/**
	 * Set the passenger of this vehicle.
	 *
	 * @param passenger The new passenger.
	 * @return False if it could not be done for whatever reason.
	 */
	@Override
	boolean setPassenger(Entity passenger);

}