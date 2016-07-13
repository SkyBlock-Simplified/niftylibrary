package net.netcoding.nifty.common.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;

/**
 * A Creatures inventory.
 */
public interface EntityEquipment {

	/**
	 * Clears the entity of all armor and held items.
	 */
	void clear();

	/**
	 * Gets a copy of all worn armor.
	 *
	 * @return The array of worn armor.
	 */
	ItemStack[] getArmorContents();

	/**
	 * Gets a copy of the boots currently being worn by the entity
	 *
	 * @return The boots being worn
	 */
	ItemStack getBoots();

	/**
	 * Gets the chance of the boots being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the boots being dropped (1 for players).
	 */
	float getBootsDropChance();

	/**
	 * Gets a copy of the chest plate currently being worn by the entity.
	 *
	 * @return The chest plate being worn.
	 */
	ItemStack getChestplate();

	/**
	 * Gets the chance of the chest plate being dropped upon this creature's
	 * death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the chest plate being dropped (1 for players).
	 */
	float getChestplateDropChance();

	/**
	 * Gets a copy of the helmet currently being worn by the entity.
	 *
	 * @return The helmet being worn.
	 */
	ItemStack getHelmet();

	/**
	 * Gets the chance of the helmet being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the helmet being dropped (1 for players).
	 */
	float getHelmetDropChance();

	/**
	 * Get the entity this EntityEquipment belongs to.
	 *
	 * @return The entity this EntityEquipment belongs to.
	 */
	Entity getHolder();

	/**
	 * Gets a copy of the item the entity is currently holding.
	 *
	 * @return The currently held item.
	 */
	ItemStack getItemInHand();

	/**
	 * Gets item drop chance.
	 *
	 * @return The drop chance.
	 */
	float getItemInHandDropChance();

	/**
	 * Gets a copy of the item the entity is currently holding in their main hand.
	 *
	 * @return The currently held item.
	 */
	default ItemStack getItemInMainHand() {
		return this.getItemInHand();
	}

	/**
	 * Gets the chance of the main hand item being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the currently held item being dropped (1 for players).
	 */
	default float getItemInMainHandDropChance() {
		return this.getItemInHandDropChance();
	}

	/**
	 * Gets a copy of the item the entity is currently holding in their off hand.
	 *
	 * @return The currently held item.
	 */
	ItemStack getItemInOffHand();

	/**
	 * Gets the chance of the off hand item being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the off hand item being dropped (1 for players).
	 */
	float getItemInOffHandDropChance();

	/**
	 * Gets a copy of the leggings currently being worn by the entity.
	 *
	 * @return The leggings being worn.
	 */
	ItemStack getLeggings();

	/**
	 * Gets the chance of the leggings being dropped upon this creature's
	 * death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @return Chance of the leggings being dropped (1 for players).
	 */
	float getLeggingsDropChance();

	/**
	 * Sets the entities armor to the provided array of ItemStacks.
	 *
	 * @param items The items to set the armor as.
	 */
	void setArmorContents(ItemStack[] items);

	/**
	 * Sets the boots worn by the entity.
	 *
	 * @param item The boots to put on the entity.
	 */
	void setBoots(ItemStack item);

	/**
	 * Sets the chance of the boots being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of the boots being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setBootsDropChance(float chance);

	/**
	 * Sets the chest plate worn by the entity.
	 *
	 * @param item The chest plate to put on the entity.
	 */
	void setChestplate(ItemStack item);

	/**
	 * Sets the chance of the chest plate being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of the chest plate being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setChestplateDropChance(float chance);

	/**
	 * Sets the helmet worn by the entity.
	 *
	 * @param item The helmet to put on the entity.
	 */
	void setHelmet(ItemStack item);

	/**
	 * Sets the chance of the helmet being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of the helmet being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setHelmetDropChance(float chance);

	/**
	 * Sets the item the entity is holding.
	 *
	 * @param item The item to put into the entities hand.
	 * @see #setItemInMainHand(ItemStack)
	 * @see #setItemInOffHand(ItemStack)
	 */
	void setItemInHand(ItemStack item);

	/**
	 * Sets the chance of the item this creature is currently holding being dropped upon this creature's death.
	 *
	 * @param chance The drop chance.
	 * @see #setItemInMainHandDropChance(float)
	 * @see #setItemInOffHandDropChance(float)
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setItemInHandDropChance(float chance);

	/**
	 * Sets the item the entity is holding in their main hand.
	 *
	 * @param item The item to put into the entities hand.
	 */
	default void setItemInMainHand(ItemStack item) {
		this.setItemInHand(item);
	}

	/**
	 * Sets the chance of the item this creature is currently holding in their main hand being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of the main hand item being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	default void setItemInMainHandDropChance(float chance) {
		this.setItemInHandDropChance(chance);
	}

	/**
	 * Sets the item the entity is holding in their off hand.
	 *
	 * @param item The item to put into the entities hand.
	 */
	void setItemInOffHand(ItemStack item);

	/**
	 * Sets the chance of the off hand item being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of the off hand item being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setItemInOffHandDropChance(float chance);

	/**
	 * Sets the leggings worn by the entity.
	 *
	 * @param item The leggings to put on the entity.
	 */
	void setLeggings(ItemStack item);

	/**
	 * Sets the chance of the leggings being dropped upon this creature's death.
	 *
	 * <ul>
	 * <li>A drop chance of 0F will never drop.
	 * <li>A drop chance of 1F will always drop.
	 * </ul>
	 *
	 * @param chance The chance of leggings being dropped.
	 * @throws UnsupportedOperationException When called on players.
	 */
	void setLeggingsDropChance(float chance);

}