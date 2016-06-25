package net.netcoding.nifty.common._new_.minecraft.entity.projectile;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;

/**
 * Represents a Shulker Bullet.
 */
public interface ShulkerBullet extends Projectile {

	/**
	 * Retrieve the target of this bullet.
	 *
	 * @return The targeted entity.
	 */
	Entity getTarget();

	/**
	 * Sets the target of this bullet.
	 *
	 * @param target The targeted entity.
	 */
	void setTarget(Entity target);

}