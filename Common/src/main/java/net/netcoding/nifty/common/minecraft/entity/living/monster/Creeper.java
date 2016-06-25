package net.netcoding.nifty.common.minecraft.entity.living.monster;

/**
 * Represents a Creeper.
 */
public interface Creeper extends Monster {

	/**
	 * Checks if this Creeper is powered (Electrocuted)
	 *
	 * @return True if this creeper is powered.
	 */
	boolean isPowered();

	/**
	 * Sets the Powered status of this Creeper
	 *
	 * @param powered New Powered status.
	 */
	void setPowered(boolean powered);

}