package net.netcoding.nifty.common._new_.minecraft.inventory.item.meta;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common._new_.minecraft.material.Material;

import java.util.Map;

/**
 * EnchantmentMeta is specific to items that can <i>store</i> enchantments, as
 * opposed to being enchanted. {@link Material#ENCHANTED_BOOK} is an example
 * of an item with enchantment storage.
 */
public interface EnchantmentStorageMeta extends ItemMeta {

	/**
	 * Stores the specified enchantment in this item meta.
	 *
	 * @param enchantment Enchantment to store.
	 * @param level Level for the enchantment.
	 * @param ignoreLevelRestriction This indicates the enchantment should be applied, ignoring the level limit.
	 * @return True if the item meta changed as a result of this call.
	 */
	boolean addStoredEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction);

	@Override
	EnchantmentStorageMeta clone();

	/**
	 * Checks for the level of the stored enchantment.
	 *
	 * @param enchantment Enchantment to check.
	 * @return The level that the specified stored enchantment has, or 0 if none.
	 */
	default int getStoredEnchantLevel(Enchantment enchantment) {
		return this.getStoredEnchants().get(enchantment);
	}

	/**
	 * Gets a copy the stored enchantments in this ItemMeta.
	 *
	 * @return An immutable copy of the stored enchantments
	 */
	Map<Enchantment, Integer> getStoredEnchants();

	/**
	 * Checks if the specified enchantment conflicts with any enchantments in this ItemMeta.
	 *
	 * @param enchantment Enchantment to test.
	 * @return True if the enchantment conflicts.
	 */
	default boolean hasConflictingStoredEnchant(Enchantment enchantment) {
		for (Enchantment enchant : this.getEnchants().keySet()) {
			if (enchant.conflictsWith(enchantment))
				return true;
		}

		return false;
	}

	/**
	 * Checks for storage of the specified enchantment.
	 *
	 * @param enchantment Enchantment to check.
	 * @return True if this enchantment is stored in this meta.
	 */
	default boolean hasStoredEnchant(Enchantment enchantment) {
		return this.getStoredEnchants().containsKey(enchantment);
	}

	/**
	 * Checks for the existence of any stored enchantments.
	 *
	 * @return True if an enchantment exists on this meta.
	 */
	boolean hasStoredEnchants();

	/**
	 * Remove the specified stored enchantment from this item meta.
	 *
	 * @param enchantment Enchantment to remove.
	 * @return True if the item meta changed as a result of this call.
	 */
	boolean removeStoredEnchant(Enchantment enchantment);

}