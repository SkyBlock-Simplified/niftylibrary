package net.netcoding.nifty.common.minecraft.entity.hanging;

import net.netcoding.nifty.common.minecraft.Art;
import net.netcoding.nifty.common.minecraft.event.hanging.HangingBreakEvent;

/**
 * Represents a Painting.
 */
public interface Painting extends Hanging {

	/**
	 * Gets the art on this painting.
	 *
	 * @return The art.
	 */
	Art getArt();

	/**
	 * Sets the art on this painting
	 *
	 * @param art The new art.
	 * @return False if the new art won't fit at the painting's current location.
	 */
	default boolean setArt(Art art) {
		return this.setArt(art, false);
	}

	/**
	 * Set the art on this painting.
	 *
	 * @param art The new art.
	 * @param force If true, force the new art regardless of whether it fits
	 *     at the current location. Note that forcing it where it can't fit
	 *     normally causes it to drop as an item unless you override this by
	 *     catching the {@link HangingBreakEvent}.
	 * @return False if force was false and the new art won't fit at the painting's current location.
	 */
	boolean setArt(Art art, boolean force);

}