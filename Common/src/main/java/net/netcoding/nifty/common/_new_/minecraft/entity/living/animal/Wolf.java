package net.netcoding.nifty.common._new_.minecraft.entity.living.animal;

import net.netcoding.nifty.common._new_.minecraft.entity.Tameable;
import net.netcoding.nifty.core.api.DyeColor;

/**
 * Represents a Wolf.
 */
public interface Wolf extends Animal, Tameable {

	/**
	 * Checks if this wolf is angry.
	 *
	 * @return True if angry.
	 */
	boolean isAngry();

	/**
	 * Sets the anger of this wolf.
	 * <p>
	 * An angry wolf can not be fed or tamed, and will actively look for targets to attack.
	 *
	 * @param angry True if angry
	 */
	void setAngry(boolean angry);

	/**
	 * Checks if this wolf is sitting.
	 *
	 * @return True if sitting.
	 */
	boolean isSitting();

	/**
	 * Sets if this wolf is sitting.
	 * <p>
	 * Will remove any path that the wolf was following beforehand.
	 *
	 * @param sitting True if sitting
	 */
	void setSitting(boolean sitting);

	/**
	 * Get the collar color of this wolf.
	 *
	 * @return The color of the collar.
	 */
	DyeColor getCollarColor();

	/**
	 * Set the collar color of this wolf.
	 *
	 * @param color the color to apply.
	 */
	void setCollarColor(DyeColor color);

}