package net.netcoding.nifty.common.minecraft.material;

import net.netcoding.nifty.core.api.DyeColor;

/**
 * Indicates that an object can be colored.
 */
public interface Colorable {

	/**
	 * Gets the color of this object.
	 *
	 * @return The DyeColor of this object.
	 */
	DyeColor getColor();

	/**
	 * Sets the color of this object to the specified DyeColor.
	 *
	 * @param color The color of the object, as a DyeColor.
	 */
	void setColor(DyeColor color);


}