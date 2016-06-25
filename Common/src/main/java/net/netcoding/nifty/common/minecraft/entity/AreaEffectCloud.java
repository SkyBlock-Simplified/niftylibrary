package net.netcoding.nifty.common.minecraft.entity;

import net.netcoding.nifty.common.minecraft.Particle;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.ProjectileSource;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionData;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffectType;
import net.netcoding.nifty.core.api.color.Color;
import net.netcoding.nifty.core.util.ListUtil;

import java.util.List;

/**
 * Represents an area effect cloud which will imbue a potion effect onto entities which enter it.
 */
public interface AreaEffectCloud extends Entity {

	/**
	 * Adds a custom potion effect to this cloud.
	 *
	 * @param effect the potion effect to add.
	 * @param overwrite true if any existing effect of the same type should be overwritten.
	 * @return true if the effect was added as a result of this call.
	 */
	boolean addCustomEffect(PotionEffect effect, boolean overwrite);

	/**
	 * Removes all custom potion effects from this cloud.
	 */
	void clearCustomEffects();

	/**
	 * Returns the potion data about the base potion.
	 *
	 * @return a PotionData object.
	 */
	PotionData getBasePotionData();

	/**
	 * Gets the color of this cloud. Will be applied as a tint to its particles.
	 *
	 * @return cloud color
	 */
	Color getColor();

	/**
	 * Gets a list containing all custom potion effects applied to this cloud.
	 *
	 * @return the immutable list of custom potion effects.
	 */
	List<PotionEffect> getCustomEffects();

	/**
	 * Gets the duration which this cloud will exist for (in ticks).
	 *
	 * @return cloud duration.
	 */
	int getDuration();

	/**
	 * Gets the amount that the duration of this cloud will decrease by when it applies an effect to an entity.
	 *
	 * @return duration on use delta.
	 */
	int getDurationOnUse();

	/**
	 * Gets the particle which this cloud will be composed of.
	 *
	 * @return particle the set particle type.
	 */
	Particle getParticle();

	/**
	 * Gets the initial radius of the cloud..
	 *
	 * @return The radius.
	 */
	float getRadius();

	/**
	 * Gets the amount that the radius of this cloud will decrease by when it applies an effect to an entity.
	 *
	 * @return The radius on use delta.
	 */
	float getRadiusOnUse();

	/**
	 * Gets the amount that the radius of this cloud will decrease by each tick.
	 *
	 * @return The radius per tick delta.
	 */
	float getRadiusPerTick();

	/**
	 * Gets the time that an entity will be immune from subsequent exposure.
	 *
	 * @return The reapplication delay.
	 */
	int getReapplicationDelay();

	/**
	 * Retrieve the original source of this cloud.
	 *
	 * @return The {@link ProjectileSource} that threw the LingeringPotion.
	 */
	ProjectileSource getSource();

	/**
	 * Gets the time which an entity has to be exposed to the cloud before the effect is applied.
	 *
	 * @return The wait time.
	 */
	int getWaitTime();

	/**
	 * Checks for a specific custom potion effect type on this cloud.
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
	 * Removes a custom potion effect from this cloud.
	 *
	 * @param type The potion effect type to remove.
	 * @return True if the an effect was removed as a result of this call.
	 */
	boolean removeCustomEffect(PotionEffectType type);

	/**
	 * Sets the underlying potion data
	 *
	 * @param data PotionData to set the base potion state to
	 */
	void setBasePotionData(PotionData data);

	/**
	 * Sets the color of this cloud. Will be applied as a tint to its particles.
	 *
	 * @param color The cloud color.
	 */
	void setColor(Color color);

	/**
	 * Sets the duration which this cloud will exist for (in ticks).
	 *
	 * @param duration The cloud duration.
	 */
	void setDuration(int duration);

	/**
	 * Sets the amount that the duration of this cloud will decrease by when it
	 * applies an effect to an entity.
	 *
	 * @param duration The duration on use delta.
	 */
	void setDurationOnUse(int duration);

	/**
	 * Sets the particle which this cloud will be composed of
	 *
	 * @param particle The new particle type.
	 */
	void setParticle(Particle particle);

	/**
	 * Sets the initial radius of the cloud.
	 *
	 * @param radius The radius.
	 */
	void setRadius(float radius);

	/**
	 * Sets the amount that the radius of this cloud will decrease by when it applies an effect to an entity.
	 *
	 * @param radius The radius on use delta.
	 */
	void setRadiusOnUse(float radius);

	/**
	 * Gets the amount that the radius of this cloud will decrease by each tick.
	 *
	 * @param radius The radius per tick delta.
	 */
	void setRadiusPerTick(float radius);

	/**
	 * Sets the time that an entity will be immune from subsequent exposure.
	 *
	 * @param delay The reapplication delay.
	 */
	void setReapplicationDelay(int delay);

	/**
	 * Set the original source of this cloud.
	 *
	 * @param source The {@link ProjectileSource} that threw the LingeringPotion.
	 */
	void setSource(ProjectileSource source);

	/**
	 * Sets the time which an entity has to be exposed to the cloud before the effect is applied.
	 *
	 * @param waitTime The wait time.
	 */
	void setWaitTime(int waitTime);

}