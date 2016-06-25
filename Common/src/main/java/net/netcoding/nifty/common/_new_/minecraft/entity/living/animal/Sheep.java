package net.netcoding.nifty.common._new_.minecraft.entity.living.animal;

import net.netcoding.nifty.common._new_.minecraft.material.Colorable;

public interface Sheep extends Animal, Colorable {

	/**
	 * Gets if the sheep is sheared.
	 *
	 * @return True if sheep is sheared.
	 */
	boolean isSheared();

	/**
	 * Sets if the sheep is sheared.
	 *
	 * @param sheared True to shear the sheep.
	 */
	void setSheared(boolean sheared);

}