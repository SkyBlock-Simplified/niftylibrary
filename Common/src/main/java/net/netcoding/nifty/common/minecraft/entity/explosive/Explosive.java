package net.netcoding.nifty.common.minecraft.entity.explosive;

import net.netcoding.nifty.common.minecraft.entity.Entity;

/**
 * Represents an explosive entity.
 */
public interface Explosive extends Entity {

	/**
	 * Set the radius affected by this explosive's explosion.
	 *
	 * @param yield The explosive yield.
	 */
	void setYield(float yield);

	/**
	 * Return the radius or yield of this explosive's explosion.
	 *
	 * @return The radius of blocks affected.
	 */
	float getYield();

	/**
	 * Set whether or not this explosive's explosion causes fire.
	 *
	 * @param value Whether it should cause fire.
	 */
	void setIncendiary(boolean value);

	/**
	 * Return whether or not this explosive creates a fire when exploding.
	 *
	 * @return True if the explosive creates fire.
	 */
	boolean isIncendiary();

}