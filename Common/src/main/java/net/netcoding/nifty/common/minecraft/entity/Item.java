package net.netcoding.nifty.common.minecraft.entity;

import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

/**
 * Represents an Item.
 */
public interface Item extends Entity {

	/**
	 * Gets the item stack associated with this item drop.
	 *
	 * @return An item stack.
	 */
	ItemStack getItemStack();

	/**
	 * Gets the delay before this Item is available to be picked up by players
	 *
	 * @return The remaining delay.
	 */
	int getPickupDelay();

	/**
	 * Sets the item stack associated with this item drop.
	 *
	 * @param item An item stack.
	 */
	void setItemStack(ItemStack item);

	/**
	 * Sets the delay before this Item is available to be picked up by players.
	 *
	 * @param delay New delay.
	 */
	void setPickupDelay(int delay);

}