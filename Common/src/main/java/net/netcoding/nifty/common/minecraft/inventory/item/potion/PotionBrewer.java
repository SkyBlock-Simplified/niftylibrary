package net.netcoding.nifty.common.minecraft.inventory.item.potion;

import java.util.Collection;

/**
 * Represents a brewer that can create {@link PotionEffect PotionEffects}.
 */
public interface PotionBrewer {

	/**
	 * Creates a {@link PotionEffect} from the given {@link PotionEffectType}, applying duration modifiers and checks.
	 *
	 * @param potion The type of potion.
	 * @param duration The duration in ticks.
	 * @param amplifier The amplifier of the effect.
	 * @return The resulting potion effect.
	 */
	PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier);

	/**
	 * Returns a collection of {@link PotionEffect} that would be applied from a potion with the given type.
	 *
	 * @param type The type of the potion.
	 * @return The list of effects.
	 */
	Collection<PotionEffect> getEffects(PotionType type, boolean upgraded, boolean extended);

}