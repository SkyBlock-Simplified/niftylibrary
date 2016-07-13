package net.netcoding.nifty.common.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.recipe.MerchantRecipe;

import java.util.List;

/**
 * Represents a Villager.
 */
public interface Villager extends Ageable, NPC, InventoryHolder {

	/**
	 * Gets this villager's inventory.
	 * <br>
	 * Note that this inventory is not the Merchant inventory, rather, it is the
	 * items that a villager might have collected (from harvesting crops, etc.)
	 */
	@Override
	Inventory getInventory();

	/**
	 * Gets the current profession of this villager.
	 *
	 * @return Current profession.
	 */
	Profession getProfession();

	/**
	 * Get the recipe at a certain index of this villager's trade list.
	 *
	 * @param index The index.
	 * @return The recipe.
	 */
	default MerchantRecipe getRecipe(int index) {
		return this.getRecipes().get(index);
	}

	/**
	 * Get a list of trades currently available from this villager.
	 *
	 * @return an immutable list of trades
	 */
	List<MerchantRecipe> getRecipes();

	/**
	 * Gets this villager's riches, the number of emeralds this villager has been given.
	 *
	 * @return The villager's riches.
	 */
	int getRiches();

	/**
	 * Gets the player this villager is trading with, or null if it is not currently trading.
	 *
	 * @return The trader, or null.
	 */
	HumanEntity getTrader();

	/**
	 * Gets whether this villager is currently trading.
	 *
	 * @return True if the villager is trading.
	 */
	boolean isTrading();

	/**
	 * Sets the new profession of this villager.
	 *
	 * @param profession New profession.
	 */
	void setProfession(Profession profession);

	/**
	 * Set the recipe at a certain index of this villager's trade list.
	 *
	 * @param index The index.
	 * @param recipe The recipe.
	 */
	void setRecipe(int index, MerchantRecipe recipe);

	/**
	 * Set the list of trades currently available from this villager.
	 * <br>
	 * This will not change the selected trades of players currently trading with this villager.
	 *
	 * @param recipes Alist of recipes.
	 */
	void setRecipes(List<MerchantRecipe> recipes);

	/**
	 * Sets this villager's riches.
	 *
	 * @see #getRiches()
	 *
	 * @param riches The new riches.
	 */
	void setRiches(int riches);

	/**
	 * Represents the various different Villager professions there may be.
	 * Villagers have different trading options depending on their profession.
	 */
	enum Profession {

		/**
		 * Normal. <b>Reserved for Zombies.</b>
		 */
		NORMAL(true),
		/**
		 * Farmer profession. Wears a brown robe.
		 */
		FARMER(false),
		/**
		 * Librarian profession. Wears a white robe.
		 */
		LIBRARIAN(false),
		/**
		 * Priest profession. Wears a purple robe.
		 */
		PRIEST(false),
		/**
		 * Blacksmith profession. Wears a black apron.
		 */
		BLACKSMITH(false),
		/**
		 * Butcher profession. Wears a white apron.
		 */
		BUTCHER(false),
		/**
		 * Husk. <b>Reserved for Zombies</b>
		 */
		HUSK(true);

		private final boolean zombie;

		Profession(boolean zombie) {
			this.zombie = zombie;
		}

		/**
		 * Returns if this profession can only be used by zombies.
		 *
		 * @return zombie profession status
		 */
		public boolean isZombie() {
			return zombie;
		}

	}

}