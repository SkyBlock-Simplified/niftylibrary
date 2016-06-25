package net.netcoding.nifty.common._new_.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common._new_.minecraft.entity.explosive.Explosive;
import net.netcoding.nifty.common._new_.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.core.util.misc.Vector;

/**
 * Represents a Fireball.
 */
public interface Fireball extends Projectile, Explosive {

	/**
	 * Fireballs fly straight and do not take {@link #setVelocity(Vector)} well.
	 *
	 * @param direction The direction this fireball is flying toward.
	 */
	void setDirection(Vector direction);

	/**
	 * Retrieve the direction this fireball is heading toward.
	 *
	 * @return The direction.
	 */
	Vector getDirection();

}