package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.FireworkEffect;

public interface FireworkEffectMeta extends ItemMeta {

	@Override
	FireworkEffectMeta clone();

	/**
	 * Gets the firework effect for this meta.
	 *
	 * @return the current effect, or null if none
	 */
	FireworkEffect getEffect();

	/**
	 * Checks if this meta has an effect.
	 *
	 * @return True if this meta has an effect;
	 */
	default boolean hasEffect() {
		return this.getEffect() != null;
	}

	/**
	 * Sets the firework effect for this meta.
	 *
	 * @param effect The effect to set, or null to indicate none.
	 */
	void setEffect(FireworkEffect effect);

}