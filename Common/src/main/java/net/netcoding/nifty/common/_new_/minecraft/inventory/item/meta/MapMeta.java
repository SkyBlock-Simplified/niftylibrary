package net.netcoding.nifty.common._new_.minecraft.inventory.item.meta;

/**
 * Represents a map that can be scalable.
 */
public interface MapMeta extends ItemMeta {

	@Override
	MapMeta clone();

	/**
	 * Checks to see if this map is scaling.
	 *
	 * @return true if this map is scaling
	 */
	boolean isScaling();

	/**
	 * Sets if this map is scaling or not.
	 *
	 * @param value true to scale
	 */
	void setScaling(boolean value);

}