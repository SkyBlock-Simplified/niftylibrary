package net.netcoding.nifty.common.minecraft.entity.projectile;

/**
 * Represents a Spectral Arrow.
 */
public interface SpectralArrow extends Arrow {

	/**
	 * Returns the amount of time that this arrow will apply the glowing effect for.
	 *
	 * @return The glowing effect ticks.
	 */
	int getGlowingTicks();

	/**
	 * Sets the amount of time to apply the glowing effect for.
	 *
	 * @param duration The glowing effect ticks.
	 */
	void setGlowingTicks(int duration);

}