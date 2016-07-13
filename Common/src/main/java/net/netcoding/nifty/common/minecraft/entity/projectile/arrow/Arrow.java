package net.netcoding.nifty.common.minecraft.entity.projectile.arrow;

import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;

/**
 * Represents an arrow.
 */
public interface Arrow extends Projectile {

	/**
	 * Gets the knockback strength for an arrow, which is the
	 * {@link Enchantment#KNOCKBACK KnockBack} level
	 * of the bow that shot it.
	 *
	 * @return The knockback strength value.
	 */
	int getKnockbackStrength();

	/**
	 * Gets whether this arrow is critical.
	 * <p>
	 * Critical arrows have increased damage and cause particle effects.
	 * <p>
	 * Critical arrows generally occur when a player fully draws a bow before firing.
	 *
	 * @return True if critical.
	 */
	boolean isCritical();

	/**
	 * Sets whether or not this arrow should be critical.
	 *
	 * @param value True if it should be critical.
	 */
	void setCritical(boolean value);

	/**
	 * Sets the knockback strength for an arrow.
	 *
	 * @param knockbackStrength The knockback strength value.
	 */
	void setKnockbackStrength(int knockbackStrength);

}