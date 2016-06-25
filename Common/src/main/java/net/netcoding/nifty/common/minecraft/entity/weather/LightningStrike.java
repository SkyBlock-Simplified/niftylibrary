package net.netcoding.nifty.common.minecraft.entity.weather;

/**
 * Represents a Lightning Strike.
 */
public interface LightningStrike extends Weather {

	/**
	 * Returns whether the strike is an effect that does no damage.
	 *
	 * @return True if the strike is an effect.
	 */
	boolean isEffect();

}