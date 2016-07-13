package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.potion.PotionData;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.potion.PotionType;
import net.netcoding.nifty.core.util.ListUtil;

import java.util.List;

public interface PotionMeta extends ItemMeta {

	/**
	 * Adds a custom potion effect to this potion.
	 *
	 * @param effect the potion effect to add
	 * @param overwrite true if any existing effect of the same type should be
	 * overwritten
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean addCustomEffect(PotionEffect effect, boolean overwrite);

	/**
	 * Removes all custom potion effects from this potion.
	 *
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean clearCustomEffects();

	@Override
	PotionMeta clone();

	/**
	 * Returns the potion data about the base potion
	 *
	 * @return a PotionData object
	 */
	PotionData getBasePotionData();

	/**
	 * Gets an immutable list containing all custom potion effects applied to
	 * this potion.
	 * <p>
	 * Plugins should check that hasCustomEffects() returns true before calling
	 * this method.
	 *
	 * @return the immutable list of custom potion effects
	 */
	List<PotionEffect> getCustomEffects();

	/**
	 * Checks for a specific custom potion effect type on this potion.
	 *
	 * @param type the potion effect type to check for
	 * @return true if the potion has this effect
	 */
	default boolean hasCustomEffect(PotionEffectType type) {
		return this.getCustomEffects().stream().anyMatch(effect -> effect.getType().equals(type));
	}

	/**
	 * Checks for the presence of custom potion effects.
	 *
	 * @return true if custom potion effects are applied
	 */
	default boolean hasCustomEffects() {
		return ListUtil.notEmpty(this.getCustomEffects());
	}

	/**
	 * Sets the underlying potion data
	 *
	 * @param data PotionData to set the base potion state to
	 */
	void setBasePotionData(PotionData data);

	/**
	 * Removes a custom potion effect from this potion.
	 *
	 * @param type the potion effect type to remove
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean removeCustomEffect(PotionEffectType type);

	/**
	 * Moves a potion effect to the top of the potion effect list.
	 * <p>
	 * This causes the client to display the potion effect in the potion's name.
	 *
	 * @param type the potion effect type to move
	 * @return true if the potion meta changed as a result of this call
	 * @deprecated use {@link PotionType#PotionType}
	 */
	@Deprecated
	boolean setMainEffect(PotionEffectType type);

}