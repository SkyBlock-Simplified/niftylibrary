package net.netcoding.nifty.common._new_.minecraft.entity.hanging;

import net.netcoding.nifty.common._new_.minecraft.Rotation;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

/**
 * Represents an Item Frame.
 */
public interface ItemFrame extends Hanging {

	/**
	 * Get the item in this frame.
	 *
	 * @return A copy the item in this item frame.
	 */
	ItemStack getItem();

	/**
	 * Get the rotation of the frame's item.
	 *
	 * @return The direction.
	 */
	Rotation getRotation();

	/**
	 * Set the item in this frame
	 *
	 * @param item The new item.
	 */
	void setItem(ItemStack item);

	/**
	 * Set the rotation of the frame's item.
	 *
	 * @param rotation The new rotation.
	 */
	void setRotation(Rotation rotation);

}