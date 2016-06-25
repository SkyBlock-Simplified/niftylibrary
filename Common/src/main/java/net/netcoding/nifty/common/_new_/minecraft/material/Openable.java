package net.netcoding.nifty.common._new_.minecraft.material;

/**
 * Doors that can be opened.
 */
public interface Openable {

	/**
	 * Check to see if the door is open.
	 *
	 * @return true if the door has swung counterclockwise around its hinge.
	 */
	boolean isOpen();

	/**
	 * Configure this door to be either open or closed;
	 *
	 * @param value True to open the door.
	 */
	void setOpen(boolean value);

}