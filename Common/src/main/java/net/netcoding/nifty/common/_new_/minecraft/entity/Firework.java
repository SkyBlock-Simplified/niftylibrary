package net.netcoding.nifty.common._new_.minecraft.entity;

import net.netcoding.nifty.common._new_.minecraft.inventory.item.meta.FireworkMeta;

/**
 * Represents a firework.
 */
public interface Firework extends Entity {

	/**
	 * Cause this firework to explode at earliest opportunity, as if it has no remaining fuse.
	 */
	void detonate();

	/**
	 * Get a copy of the fireworks meta.
	 *
	 * @return A copy of the current Firework meta.
	 */
	FireworkMeta getFireworkMeta();

	/**
	 * Apply the provided meta to the fireworks.
	 *
	 * @param meta The FireworkMeta to apply.
	 */
	void setFireworkMeta(FireworkMeta meta);

}