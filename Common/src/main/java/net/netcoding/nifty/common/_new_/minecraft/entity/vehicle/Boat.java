package net.netcoding.nifty.common._new_.minecraft.entity.vehicle;

import net.netcoding.nifty.common._new_.minecraft.TreeSpecies;

/**
 * Represents a Boat.
 */
public interface Boat extends Vehicle {

	/**
	 * Gets the maximum speed of a boat. The speed is unrelated to the
	 * velocity.
	 *
	 * @return The max speed.
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	double getMaxSpeed();

	/**
	 * Gets the deceleration rate (newSpeed = curSpeed * rate) of occupied
	 * boats. The default is 0.2.
	 *
	 * @return The rate of deceleration.
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	double getOccupiedDeceleration();

	/**
	 * Gets the deceleration rate (newSpeed = curSpeed * rate) of unoccupied
	 * boats. The default is -1. Values below 0 indicate that no additional
	 * deceleration is imposed.
	 *
	 * @return The rate of deceleration
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	double getUnoccupiedDeceleration();

	/**
	 * Gets the wood type of the boat.
	 *
	 * @return The wood type.
	 */
	TreeSpecies getWoodType();

	/**
	 * Get whether boats can work on land.
	 *
	 * @return Whether boats can work on land
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	boolean getWorkOnLand();

	/**
	 * Sets the maximum speed of a boat. Must be nonnegative. Default is 0.4D.
	 *
	 * @param speed The max speed.
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	void setMaxSpeed(double speed);

	/**
	 * Sets the deceleration rate (newSpeed = curSpeed * rate) of occupied
	 * boats. Setting this to a higher value allows for quicker acceleration.
	 * The default is 0.2.
	 *
	 * @param rate The deceleration rate.
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	void setOccupiedDeceleration(double rate);

	/**
	 * Sets the deceleration rate (newSpeed = curSpeed * rate) of unoccupied
	 * boats. Setting this to a higher value allows for quicker deceleration
	 * of boats when a player disembarks. The default is -1. Values below 0
	 * indicate that no additional deceleration is imposed.
	 *
	 * @param rate The deceleration rate.
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	void setUnoccupiedDeceleration(double rate);

	/**
	 * Sets the wood type of the boat.
	 *
	 * @param species The new wood type.
	 */
	void setWoodType(TreeSpecies species);

	/**
	 * Set whether boats can work on land.
	 *
	 * @param workOnLand Whether boats can work on land
	 * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
	 */
	@Deprecated
	void setWorkOnLand(boolean workOnLand);

}