package net.netcoding.nifty.common.minecraft.inventory.type;

import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

/**
 * Player Inventory
 */
public interface PlayerInventory extends Inventory {

	/**
	 * Get all ItemStacks from the armor slots.
	 *
	 * @return All the ItemStacks from the armor slots.
	 */
	ItemStack[] getArmorContents();

	/**
	 * Return the ItemStack from the boots slot.
	 *
	 * @return The ItemStack in the boots slot.
	 */
	ItemStack getBoots();

	/**
	 * Return the ItemStack from the chestplate slot.
	 *
	 * @return The ItemStack in the chestplate slot.
	 */
	ItemStack getChestplate();

	/**
	 * Get all additional ItemStacks stored in this inventory.
	 * <br>
	 * What defines an extra slot is up to the implementation, however it
	 * will not be contained within {@link #getStorageContents()} or
	 * {@link #getArmorContents()}.
	 *
	 * @return All additional ItemStacks.
	 */
	ItemStack[] getExtraContents();

	/**
	 * Get the slot number of the currently held item.
	 *
	 * @return Held item slot number.
	 */
	int getHeldItemSlot();

	/**
	 * Return the ItemStack from the helmet slot.
	 *
	 * @return The ItemStack in the helmet slot.
	 */
	ItemStack getHelmet();

	/**
	 * Gets a copy of the item the player is currently holding.
	 *
	 * @return The currently held item.
	 * @see #getItemInMainHand()
	 * @see #getItemInOffHand()
	 */
	ItemStack getItemInHand();

	/**
	 * Gets a copy of the item the player is currently holding in their main hand.
	 *
	 * @return The currently held item.
	 */
	default ItemStack getItemInMainHand() {
		return this.getItemInHand();
	}

	/**
	 * Gets a copy of the item the player is currently holding in their off hand.
	 *
	 * @return The currently held offhand item.
	 */
	ItemStack getItemInOffHand();

	/**
	 * Return the ItemStack from the leg slot.
	 *
	 * @return The ItemStack in the leg slot.
	 */
	ItemStack getLeggings();

	/**
	 * Put the given ItemStacks into the armor slots.
	 *
	 * @param items The ItemStacks to use as armour.
	 */
	void setArmorContents(ItemStack[] items);

	/**
	 * Put the given ItemStack into the boots slot.
	 * <p>
	 * This does not check if the ItemStack is a boots.
	 *
	 * @param boots The ItemStack to use as boots.
	 */
	void setBoots(ItemStack boots);

	/**
	 * Put the given ItemStack into the chestplate slot.
	 * <p>
	 * This does not check if the ItemStack is a chestplate.
	 *
	 * @param chestplate The ItemStack to use as chestplate.
	 */
	void setChestplate(ItemStack chestplate);

	/**
	 * Put the given ItemStacks into the extra slots.
	 * <br>
	 * See {@link #getExtraContents()} for an explanation of extra slots.
	 *
	 * @param items The ItemStacks to use as extra.
	 */
	void setExtraContents(ItemStack[] items);

	/**
	 * Set the slot number of the currently held item.
	 * <p>
	 * This validates whether the slot is between 0 and 8 inclusive.
	 *
	 * @param slot The new slot number.
	 * @throws IllegalArgumentException If slot is not between 0 and 8 inclusive.
	 */
	void setHeldItemSlot(int slot);

	/**
	 * Put the given ItemStack into the helmet slot.
	 * <p>
	 * This does not check if the ItemStack is a helmet.
	 *
	 * @param helmet The ItemStack to use as helmet.
	 */
	void setHelmet(ItemStack helmet);

	/**
	 * Stores the ItemStack at the given index of the inventory.
	 * <p>
	 * Indexes 0 through 8 refer to the hotbar. 9 through 35 refer to the main inventory, counting up from 9 at the top
	 * left corner of the inventory, moving to the right, and moving to the row below it back on the left side when it
	 * reaches the end of the row. It follows the same path in the inventory like you would read a book.
	 * <p>
	 * Indexes 36 through 39 refer to the armor slots. Though you can set armor with this method using these indexes,
	 * you are encouraged to use the provided methods for those slots.
	 * <p>
	 * If you attempt to use this method with an index less than 0 or greater than 39, an ArrayIndexOutOfBounds
	 * exception will be thrown.
	 *
	 * @param index The index where to put the ItemStack.
	 * @param item The ItemStack to set.
	 * @throws ArrayIndexOutOfBoundsException when index &lt; 0 || index &gt; 39
	 * @see #setBoots(ItemStack)
	 * @see #setChestplate(ItemStack)
	 * @see #setHelmet(ItemStack)
	 * @see #setLeggings(ItemStack)
	 */
	@Override
	void setItem(int index, ItemStack item) throws ArrayIndexOutOfBoundsException;

	/**
	 * Sets the item the player is holding.
	 *
	 * @param item The item to put into the player's hand.
	 * @see #setItemInMainHand(ItemStack)
	 * @see #setItemInOffHand(ItemStack)
	 */
	void setItemInHand(ItemStack item);

	/**
	 * Sets the item the player is holding in their main hand.
	 *
	 * @param item The item to put into the player's hand.
	 */
	default void setItemInMainHand(ItemStack item) {
		this.setItemInHand(item);
	}

	/**
	 * Sets the item the player is holding in their off hand.
	 *
	 * @param item The item to put into the player's offhand.
	 */
	void setItemInOffHand(ItemStack item);

	/**
	 * Put the given ItemStack into the leg slot.
	 * <p>
	 * This does not check if the ItemStack is a pair of leggings.
	 *
	 * @param leggings The ItemStack to use as leggings.
	 */
	void setLeggings(ItemStack leggings);

}