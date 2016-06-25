package net.netcoding.nifty.common._new_.minecraft.entity.living.monster;

/**
 * Represents a Guardian.
 */
public interface Guardian extends Monster {

	/**
	 * Check if the Guardian is an Elder Guardian.
	 *
	 * @return True if the Guardian is an Elder Guardian.
	 */
	boolean isElder();

	/**
	 * Set the Guardian to an Elder Guardian or not.
	 *
	 * @param elder True if this Guardian should be a Elder Guardian.
	 */
	void setElder(boolean elder);

}