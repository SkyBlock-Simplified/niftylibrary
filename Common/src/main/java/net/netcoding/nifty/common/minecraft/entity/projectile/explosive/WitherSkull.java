package net.netcoding.nifty.common.minecraft.entity.projectile.explosive;

/**
 * Represents a wither skull {@link Fireball}.
 */
public interface WitherSkull extends Fireball {

	/**
	 * Sets the charged status of the wither skull.
	 *
	 * @param charged Whether it should be charged.
	 */
	void setCharged(boolean charged);

	/**
	 * Gets whether or not the wither skull is charged.
	 *
	 * @return True if the wither skull is charged
	 */
	boolean isCharged();

}