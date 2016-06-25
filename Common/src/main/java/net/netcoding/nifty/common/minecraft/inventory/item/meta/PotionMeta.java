package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionData;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionType;

import java.util.List;

public interface PotionMeta extends ItemMeta {

	@Override
	PotionMeta clone();

	/**
	 * Sets the underlying potion data
	 *
	 * @param data PotionData to set the base potion state to
	 */
	void setBasePotionData(PotionData data);

	/**
	 * Returns the potion data about the base potion
	 *
	 * @return a PotionData object
	 */
	PotionData getBasePotionData();

	/**
	 * Checks for the presence of custom potion effects.
	 *
	 * @return true if custom potion effects are applied
	 */
	boolean hasCustomEffects();

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
	 * Adds a custom potion effect to this potion.
	 *
	 * @param effect the potion effect to add
	 * @param overwrite true if any existing effect of the same type should be
	 * overwritten
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean addCustomEffect(PotionEffect effect, boolean overwrite);

	/**
	 * Removes a custom potion effect from this potion.
	 *
	 * @param type the potion effect type to remove
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean removeCustomEffect(PotionEffectType type);

	/**
	 * Checks for a specific custom potion effect type on this potion.
	 *
	 * @param type the potion effect type to check for
	 * @return true if the potion has this effect
	 */
	boolean hasCustomEffect(PotionEffectType type);

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

	/**
	 * Removes all custom potion effects from this potion.
	 *
	 * @return true if the potion meta changed as a result of this call
	 */
	boolean clearCustomEffects();

}