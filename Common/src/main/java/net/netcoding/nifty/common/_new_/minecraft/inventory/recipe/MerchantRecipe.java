package net.netcoding.nifty.common._new_.minecraft.inventory.recipe;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;

import java.util.List;

public interface MerchantRecipe extends Recipe {

	void addIngredient(ItemStack item);

	List<ItemStack> getIngredients();

	/**
	 * Get the maximum number of uses this trade has.
	 * <br>
	 * The maximum uses of this trade may increase when a player trades with the owning villager.
	 *
	 * @return The maximum number of uses.
	 */
	int getMaxUses();

	/**
	 * Get the number of times this trade has been used.
	 *
	 * @return The number of uses.
	 */
	int getUses();

	/**
	 * Whether to reward experience for the trade.
	 *
	 * @return True if rewarded experience for completing this trade.
	 */
	boolean hasExperienceReward();

	void removeIngredient(int index);

	/**
	 * Set whether to reward experience for the trade.
	 *
	 * @param flag Whether to reward experience for completing this trade.
	 */
	void setExperienceReward(boolean flag);

	void setIngredients(List<ItemStack> ingredients);

	/**
	 * Set the maximum number of uses this trade has.
	 *
	 * @param maxUses The maximum number of time this trade can be used.
	 */
	void setMaxUses(int maxUses);

	/**
	 * Set the number of times this trade has been used.
	 *
	 * @param uses The number of uses.
	 */
	void setUses(int uses);

}