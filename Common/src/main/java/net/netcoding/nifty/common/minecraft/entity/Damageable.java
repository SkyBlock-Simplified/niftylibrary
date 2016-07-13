package net.netcoding.nifty.common.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragon;
import net.netcoding.nifty.common.minecraft.entity.living.monster.Wither;

/**
 * Represents an {@link Entity} that has health and can take damage.
 */
public interface Damageable extends Entity {

	/**
	 * Deals the given amount of damage to this entity.
	 *
	 * @param amount Amount of damage to deal.
	 */
	default void damage(double amount) {
		this.damage(amount, null);
	}

	/**
	 * Deals the given amount of damage to this entity, from a specified entity.
	 *
	 * @param amount Amount of damage to deal.
	 * @param source Entity which to attribute this damage from.
	 */
	void damage(double amount, Entity source);

	/**
	 * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
	 *
	 * @return Health represented from 0 to {@link #getMaxHealth()}.
	 */
	double getHealth();

	/**
	 * Gets the maximum health this entity has.
	 *
	 * @return Maximum health.
	 */
	double getMaxHealth();

	/**
	 * Resets the max health to the original amount.
	 */
	void resetMaxHealth();

	/**
	 * Sets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
	 *
	 * @param health New health represented from 0 to max.
	 */
	void setHealth(double health);

	/**
	 * Sets the maximum health this entity can have.
	 * <p>
	 * If the health of the entity is above the value provided it will be set to that value.
	 * <p>
	 * Note: An entity with a health bar ({@link Player}, {@link EnderDragon},
	 * {@link Wither}, etc...} will have their bar scaled accordingly.
	 *
	 * @param maxHealth Amount of health to set the maximum to.
	 */
	void setMaxHealth(double maxHealth);

}