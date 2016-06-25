package net.netcoding.nifty.common._new_.minecraft.entity.projectile;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.potion.PotionData;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.potion.PotionEffect;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.potion.PotionEffectType;
import net.netcoding.nifty.core.util.ListUtil;

import java.util.List;

/**
 * Represents a tipped arrow.
 */
public interface TippedArrow extends Arrow {

	/**
	 * Adds a custom potion effect to this arrow.
	 *
	 * @param effect The potion effect to add.
	 * @param overwrite True if any existing effect of the same type should be overwritten.
	 * @return True if the effect was added as a result of this call.
	 */
	boolean addCustomEffect(PotionEffect effect, boolean overwrite);

	/**
	 * Removes all custom potion effects from this arrow.
	 */
	void clearCustomEffects();

	/**
	 * Returns the potion data about the base potion.
	 *
	 * @return A PotionData object.
	 */
	PotionData getBasePotionData();

	/**
	 * Gets an immutable list containing all custom potion effects applied to this arrow.
	 * <p>
	 * Plugins should check that hasCustomEffects() returns true before calling this method.
	 *
	 * @return The immutable list of custom potion effects.
	 */
	List<PotionEffect> getCustomEffects();

	/**
	 * Checks for a specific custom potion effect type on this arrow.
	 *
	 * @param type The potion effect type to check for.
	 * @return True if the potion has this effect.
	 */
	default boolean hasCustomEffect(PotionEffectType type) {
		for (PotionEffect effect : this.getCustomEffects()) {
			if (effect.getType().equals(type))
				return true;
		}

		return false;
	}

	/**
	 * Checks for the presence of custom potion effects.
	 *
	 * @return True if custom potion effects are applied.
	 */
	default boolean hasCustomEffects() {
		return ListUtil.notEmpty(this.getCustomEffects());
	}

	/**
	 * Removes a custom potion effect from this arrow.
	 *
	 * @param type The potion effect type to remove.
	 * @return True if the an effect was removed as a result of this call.
	 */
	boolean removeCustomEffect(PotionEffectType type);

	/**
	 * Sets the underlying potion data.
	 *
	 * @param data PotionData to set the base potion state to.
	 */
	void setBasePotionData(PotionData data);

}