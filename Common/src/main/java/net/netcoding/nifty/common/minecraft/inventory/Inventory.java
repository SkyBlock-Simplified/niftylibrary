package net.netcoding.nifty.common.minecraft.inventory;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Inventory
 */
public interface Inventory {//extends Iterable<ItemStack> {

	/**
	 * Stores the given ItemStacks in the inventory. This will try to fill
	 * existing stacks and empty slots as well as it can.
	 * <p>
	 * The returned HashMap contains what it couldn't store, where the key is
	 * the index of the parameter, and the value is the ItemStack at that
	 * index of the varargs parameter. If all items are stored, it will return
	 * an empty HashMap.
	 * <p>
	 * If you pass in ItemStacks which exceed the maximum stack size for the
	 * Material, first they will be added to partial stacks where
	 * Material.getMaxStackSize() is not exceeded, up to
	 * Material.getMaxStackSize(). When there are no partial stacks left
	 * stacks will be split on Inventory.getMaxStackSize() allowing you to
	 * exceed the maximum stack size for that material.
	 * <p>
	 * It is known that in some implementations this method will also set
	 * the inputted argument amount to the number of that item not placed in
	 * slots.
	 *
	 * @param items The ItemStacks to add
	 * @return A HashMap containing items that didn't fit.
	 * @throws IllegalArgumentException if items or any element in it is null
	 */
	Map<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException;

	/**
	 * Returns a Map with all slots and ItemStacks in the inventory with the specified Material.
	 * <p>
	 * The Map contains entries where, the key is the slot index, and the
	 * value is the ItemStack in that slot. If no matching ItemStack with the
	 * given Material is found, an empty map is returned.
	 *
	 * @param material The material to look for.
	 * @return A Map containing the slot index, ItemStack pairs.
	 * @throws IllegalArgumentException If material is null.
	 */
	default Map<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		return this.all(material.getId());
	}

	/**
	 * Returns a Map with all slots and ItemStacks in the inventory with specified type id.
	 * <p>
	 * The Map contains entries where, the key is the slot index, and the
	 * value is the ItemStack in that slot. If no matching ItemStack with the
	 * given materialId is found, an empty map is returned.
	 *
	 * @param typeId The type id to look for.
	 * @return A Map containing the slot index, ItemStack pairs.
	 */
	default Map<Integer, ? extends ItemStack> all(int typeId) {
		ConcurrentMap<Integer, ItemStack> slots = Concurrent.newMap();
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; ++i) {
			if (contents[i] != null && contents[i].getTypeId() == typeId)
				slots.put(i, contents[i]);
		}

		return slots;
	}

	/**
	 * Finds all slots in the inventory containing any ItemStacks with the
	 * given ItemStack. This will only match slots if both the type and the
	 * amount of the stack match.
	 * <p>
	 * The Map contains entries where, the key is the slot index, and the
	 * value is the ItemStack in that slot. If no matching ItemStack with the
	 * given Material is found, an empty map is returned.
	 *
	 * @param item The ItemStack to match against.
	 * @return A Map from slot indexes to item at index.
	 * @throws IllegalArgumentException If item is null.
	 */
	default Map<Integer, ? extends ItemStack> all(ItemStack item) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ConcurrentMap<Integer, ItemStack> slots = Concurrent.newMap();
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; ++i) {
			if (item.equals(contents[i]))
				slots.put(i, contents[i]);
		}

		return slots;
	}

	/**
	 * Clears out the whole Inventory.
	 */
	default void clear() {
		for (int i = 0; i < this.getSize(); i++)
			this.clear(i);
	}

	/**
	 * Clears out a particular slot in the index.
	 *
	 * @param index The index to empty.
	 */
	void clear(int index);

	/**
	 * Checks if the inventory contains any ItemStacks with the given material.
	 *
	 * @param material The material to check for.
	 * @return true if an ItemStack is found with the given Material.
	 * @throws IllegalArgumentException If material is null.
	 */
	default boolean contains(Material material) throws IllegalArgumentException {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		return this.contains(material.getId());
	}

	/**
	 * Checks if the inventory contains any ItemStacks matching the given ItemStack.
	 * <p>
	 * This will only return true if both the type and the amount of the stack match.
	 *
	 * @param item The ItemStack to match against.
	 * @return True if any exactly matching ItemStacks were found.
	 * @throws IllegalArgumentException If item is null.
	 */
	default boolean contains(ItemStack item) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ItemStack[] contents = this.getStorageContents();
		return Arrays.stream(contents).anyMatch(item::equals);
	}

	/**
	 * Checks if the inventory contains any ItemStacks with the given materialId
	 *
	 * @param typeId The type id to check for.
	 * @return True if an ItemStack in this inventory contains the specified type id.
	 */
	default boolean contains(int typeId) {
		ItemStack[] contents = this.getStorageContents();
		return Arrays.stream(contents).anyMatch(item -> item != null && item.getTypeId() == typeId);
	}

	/**
	 * Checks if the inventory contains any ItemStacks with the given material, adding to at least the minimum amount specified.
	 *
	 * @param material The material to check for.
	 * @param amount The minimum amount.
	 * @return True if amount is less than 1, True if enough ItemStacks were found to add to the given amount.
	 * @throws IllegalArgumentException If material is null.
	 */
	default boolean contains(Material material, int amount) throws IllegalArgumentException {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		return this.contains(material.getId(), amount);
	}

	/**
	 * Checks if the inventory contains any ItemStacks with the given materialId, adding to at least the minimum amount specified.
	 *
	 * @param typeId The type id to check for.
	 * @param amount The minimum amount to look for.
	 * @return True if this contains any matching ItemStack with the specified type id and amount.
	 */
	default boolean contains(int typeId, int amount) {
		ItemStack[] contents = this.getStorageContents();

		for (ItemStack item : contents) {
			if (item != null && item.getTypeId() == typeId && (amount -= item.getAmount()) <= 0)
				return true;
		}

		return false;
	}

	/**
	 * Checks if the inventory contains at least the minimum amount specified of exactly matching ItemStacks.
	 * <p>
	 * An ItemStack only counts if both the type and the amount of the stack match.
	 *
	 * @param item The ItemStack to match against.
	 * @param amount How many identical stacks to check for.
	 * @return True if amount less than 1, True if amount of exactly matching ItemStacks were found.
	 * @throws IllegalArgumentException If item is null.
	 * @see #containsAtLeast(ItemStack, int)
	 */
	default boolean contains(ItemStack item, int amount) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ItemStack[] contents = this.getStorageContents();

		for (ItemStack stack : contents) {
			if (item.equals(stack)) {
				if (--amount <= 0)
					return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the inventory contains ItemStacks matching the given
	 * ItemStack whose amounts sum to at least the minimum amount specified.
	 *
	 * @param item the ItemStack to match against
	 * @param amount the minimum amount
	 * @return True if amount less than 1, True if enough ItemStacks were found to add to the given amount
	 * @throws IllegalArgumentException If item is null.
	 */
	default boolean containsAtLeast(ItemStack item, int amount) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ItemStack[] contents = this.getStorageContents();

		for (ItemStack stack : contents) {
			if (item.isSimilar(stack) && (amount -= stack.getAmount()) <= 0)
				return true;
		}

		return false;
	}

	/**
	 * Finds the first slot in the inventory containing an ItemStack with the specified material.
	 *
	 * @param material The material to look for.
	 * @return The slot index of the given Material, -1 if not found.
	 * @throws IllegalArgumentException If material is null.
	 */
	default int first(Material material) throws IllegalArgumentException {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		return this.first(material.getId());
	}

	/**
	 * Finds the first slot in the inventory containing an ItemStack with the specified materialId.
	 *
	 * @param typeId The type id to look for.
	 * @return The slot index of the given materialId, -1 if not found.
	 */
	default int first(int typeId) {
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null && contents[i].getTypeId() == typeId)
				return i;
		}

		return -1;
	}

	/**
	 * Returns the first slot in the inventory containing an ItemStack with
	 * the given stack. This will only match a slot if both the type and the
	 * amount of the stack match.
	 *
	 * @param item The ItemStack to match against.
	 * @return The slot index of the given ItemStack, -1 if not found.
	 * @throws IllegalArgumentException If item is null.
	 */
	default int first(ItemStack item) throws IllegalArgumentException {
		return this.first(item, false);
	}

	/**
	 * Returns the first slot in the inventory containing an ItemStack with
	 * the given stack. This will only match a slot if both the type and the
	 * amount of the stack match.
	 *
	 * @param item The ItemStack to match against.
	 * @param withAmount Whether to check the amount.
	 * @return The slot index of the given ItemStack, -1 if not found.
	 * @throws IllegalArgumentException If item is null.
	 */
	default int first(ItemStack item, boolean withAmount) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; i++) {
			if (withAmount && item.equals(contents[i]))
				return i;
			else if (item.isSimilar(contents[i]))
				return i;
		}

		return -1;
	}

	/**
	 * Returns the first empty Slot.
	 *
	 * @return The first empty Slot found, -1 if no empty slots.
	 */
	default int firstEmpty() {
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null)
				return i;
		}

		return -1;
	}

	/**
	 * Returns all ItemStacks from the inventory
	 *
	 * @return An array of ItemStacks from the inventory.
	 */
	ItemStack[] getContents();

	/**
	 * Gets the block or entity belonging to the open inventory
	 *
	 * @return The holder of the inventory; null if it has no holder.
	 */
	InventoryHolder getHolder();

	/**
	 * Returns the ItemStack found in the slot at the given index
	 *
	 * @param index The index of the Slot's ItemStack to return
	 * @return The ItemStack in the slot
	 */
	ItemStack getItem(int index);

	/**
	 * Get the location of the block or entity which corresponds to this inventory.
	 * <p>
	 * May return null if this container was custom created or is a virtual / subcontainer.
	 *
	 * @return The location, null if not applicable.
	 */
	Location getLocation();

	/**
	 * Returns the maximum stack size for an ItemStack in this inventory.
	 *
	 * @return The maximum size for an ItemStack in this inventory.
	 */
	int getMaxStackSize();

	/**
	 * Returns the size of the inventory
	 *
	 * @return The size of the inventory
	 */
	int getSize();

	/**
	 * Return the contents from the section of the inventory where items can
	 * reasonably be expected to be stored. In most cases this will represent
	 * the entire inventory, but in some cases it may exclude armor or result
	 * slots.
	 * <br>
	 * It is these contents which will be used for add / contains / remove
	 * methods which look for a specific stack.
	 *
	 * @return inventory storage contents
	 */
	default ItemStack[] getStorageContents() {
		return this.getContents();
	}

	/**
	 * Returns the title of this inventory.
	 *
	 * @return The title of the inventory.
	 */
	String getTitle();

	/**
	 * Returns what type of inventory this is.
	 *
	 * @return The InventoryType representing the type of inventory.
	 */
	InventoryType getType();

	/**
	 * Gets a list of players viewing the inventory. Note that a player is
	 * considered to be viewing their own inventory and internal crafting
	 * screen even when said inventory is not open. They will normally be
	 * considered to be viewing their inventory even when they have a
	 * different inventory screen open, but it's possible for customized
	 * inventory screens to exclude the viewer's inventory, so this should
	 * never be assumed to be non-empty.
	 *
	 * @return A list of HumanEntities who are viewing this Inventory.
	 */
	List<HumanEntity> getViewers();

	//@Override
	//ListIterator<ItemStack> iterator();

	/**
	 * Returns an iterator starting at the given index. If the index is
	 * positive, then the first call to next() will return the item at that
	 * index; if it is negative, the first call to previous will return the
	 * item at index (getSize() + index).
	 *
	 * @param index The index.
	 * @return An iterator.
	 */
	//ListIterator<ItemStack> iterator(int index);

	/**
	 * Removes all stacks in the inventory matching the specified material.
	 *
	 * @param material The material to remove.
	 * @throws IllegalArgumentException If material is null.
	 */
	default void remove(Material material) throws IllegalArgumentException {
		this.remove(material, (short)-1);
	}

	/**
	 * Removes all stacks in the inventory matching the specified material.
	 *
	 * @param material The material to remove.
	 * @param data The data value to match.
	 * @throws IllegalArgumentException If material is null.
	 */
	default void remove(Material material, short data) throws IllegalArgumentException {
		Preconditions.checkArgument(material != null, "Material cannot be NULL!");
		this.remove(material.getId(), data);
	}

	/**
	 * Removes all stacks in the inventory matching the specified type id.
	 *
	 * @param typeId The type id to remove.
	 */
	default void remove(int typeId) {
		this.remove(typeId, (short)-1);
	}

	/**
	 * Removes all stacks in the inventory matching the specified type id.
	 *
	 * @param typeId The type id to remove.
	 * @param data The data value to match.
	 */
	default void remove(int typeId, short data) {
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null && contents[i].getTypeId() == typeId && (data == -1 || contents[i].getDurability() == data))
				this.clear(i);
		}
	}

	/**
	 * Removes all stacks in the inventory matching the given stack.
	 * <p>
	 * This will only match a slot if both the type and the amount of the stack match.
	 *
	 * @param item The ItemStack to match against.
	 * @throws IllegalArgumentException If item is null.
	 */
	default void remove(ItemStack item) throws IllegalArgumentException {
		Preconditions.checkArgument(item != null, "Item cannot be NULL!");
		ItemStack[] contents = this.getStorageContents();

		for (int i = 0; i < contents.length; i++) {
			if (item.equals(contents[i]))
				this.clear(i);
		}
	}

	/**
	 * Removes the given ItemStacks from the inventory.
	 * <p>
	 * It will try to remove 'as much as possible' from the types and amounts you give as arguments.
	 * <p>
	 * The returned HashMap contains what it couldn't remove, where the key is
	 * the index of the parameter, and the value is the ItemStack at that
	 * index of the varargs parameter. If all the given ItemStacks are
	 * removed, it will return an empty Map.
	 * <p>
	 * It is known that in some implementations this method will also set the
	 * inputted argument amount to the number of that item not removed from slots.
	 *
	 * @param items The ItemStacks to remove.
	 * @return A HashMap containing items that couldn't be removed.
	 * @throws IllegalArgumentException If items is null or contains null.
	 */
	default Map<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		ListUtil.noNullElements(items, "Items cannot be/contains NULL!");
		ConcurrentMap<Integer, ItemStack> leftover = Concurrent.newMap();

		for (int i = 0; i < items.length; ++i) {
			ItemStack item = items[i];
			int toDelete = item.getAmount();

			while (true) {
				int first = this.first(item, false);

				if (first == -1) {
					item.setAmount(toDelete);
					leftover.put(i, item);
					break;
				}

				ItemStack itemStack = this.getItem(first);
				int amount = itemStack.getAmount();

				if (amount <= toDelete) {
					toDelete -= amount;
					this.clear(first);
				} else {
					itemStack.setAmount(amount - toDelete);
					this.setItem(first, itemStack);
					toDelete = 0;
				}

				if (toDelete <= 0)
					break;
			}
		}

		return leftover;
	}

	/**
	 * Stores the ItemStack at the given index of the inventory.
	 *
	 * @param index The index where to put the ItemStack
	 * @param item The ItemStack to set
	 */
	void setItem(int index, ItemStack item);

	/**
	 * Completely replaces the inventory's contents. Removes all existing
	 * contents and replaces it with the ItemStacks given in the array.
	 *
	 * @param items A complete replacement for the contents; the length must
	 *     be less than or equal to {@link #getSize()}.
	 * @throws IllegalArgumentException If the array has more items than the
	 *     inventory.
	 */
	void setContents(ItemStack[] items) throws IllegalArgumentException;

	/**
	 * This method allows you to change the maximum stack size for an
	 * inventory.
	 * <p>
	 * <b>Caveats:</b>
	 * <ul>
	 * <li>Not all inventories respect this value.
	 * <li>Stacks larger than 127 may be clipped when the world is saved.
	 * <li>This value is not guaranteed to be preserved; be sure to set it
	 *     before every time you want to set a slot over the max stack size.
	 * <li>Stacks larger than the default max size for this type of inventory
	 *     may not display correctly in the client.
	 * </ul>
	 *
	 * @param size The new maximum stack size for items in this inventory.
	 */
	void setMaxStackSize(int size);

	/**
	 * Put the given ItemStacks into the storage slots
	 *
	 * @param items The ItemStacks to use as storage contents
	 * @throws IllegalArgumentException If the array has more items than the
	 * inventory.
	 */
	default void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		this.setContents(items);
	}

	enum Action {

		NOTHING,
		PICKUP_ALL,
		PICKUP_SOME,
		PICKUP_HALF,
		PICKUP_ONE,
		PLACE_ALL,
		PLACE_SOME,
		PLACE_ONE,
		SWAP_WITH_CURSOR,
		DROP_ALL_CURSOR,
		DROP_ONE_CURSOR,
		DROP_ALL_SLOT,
		DROP_ONE_SLOT,
		MOVE_TO_OTHER_INVENTORY,
		HOTBAR_MOVE_AND_READD,
		HOTBAR_SWAP,
		CLONE_STACK,
		COLLECT_TO_CURSOR,
		UNKNOWN

	}

	enum ClickType {

		LEFT,
		SHIFT_LEFT,
		RIGHT,
		SHIFT_RIGHT,
		WINDOW_BORDER_LEFT,
		WINDOW_BORDER_RIGHT,
		MIDDLE,
		NUMBER_KEY,
		DOUBLE_CLICK,
		DROP,
		CONTROL_DROP,
		CREATIVE,
		UNKNOWN;

		public boolean isKeyboardClick() {
			return this == NUMBER_KEY || this == DROP || this == CONTROL_DROP;
		}

		public boolean isCreativeAction() {
			return this == MIDDLE || this == CREATIVE;
		}

		public boolean isRightClick() {
			return this == RIGHT || this == SHIFT_RIGHT;
		}

		public boolean isLeftClick() {
			return this == LEFT || this == SHIFT_LEFT || this == DOUBLE_CLICK || this == CREATIVE;
		}

		public boolean isShiftClick() {
			return this == SHIFT_LEFT || this == SHIFT_RIGHT || this == CONTROL_DROP;
		}

	}

}