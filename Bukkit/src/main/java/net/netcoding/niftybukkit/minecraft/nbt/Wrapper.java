package net.netcoding.niftybukkit.minecraft.nbt;

/**
 * Represents an object that provides a view of a native NMS class.
 */
interface Wrapper {

	/**
	 * Retrieve the underlying native NBT tag.
	 *
	 * @return The underlying NBT.
	 */
	Object getHandle();

}