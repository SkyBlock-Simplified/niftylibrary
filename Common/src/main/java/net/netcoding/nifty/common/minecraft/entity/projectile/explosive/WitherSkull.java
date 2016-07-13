package net.netcoding.nifty.common.minecraft.entity.projectile.explosive;

/**
 * Represents a wither skull {@link Fireball}.
 */
public interface WitherSkull extends Fireball {

	/**
	 * Gets whether or not the wither skull is charged.
	 *
	 * @return True if the wither skull is charged
	 */
	boolean isCharged();

	/**
	 * Sets the charged status of the wither skull.
	 *
	 * @param value Whether it should be charged.
	 */
	void setCharged(boolean value);

}