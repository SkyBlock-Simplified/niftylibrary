package net.netcoding.nifty.common._new_.minecraft.entity.living.monster;

import net.netcoding.nifty.common._new_.minecraft.entity.living.Hostile;

/**
 * Represents a Slime.
 */
public interface Slime extends Hostile {

	/**
	 * Gets the size of the slime.
	 *
	 * @return The size of the slime.
	 */
	int getSize();

	/**
	 * Sets the size of the slime.
	 *
	 * @param size The new size of the slime.
	 */
	void setSize(int size);

}