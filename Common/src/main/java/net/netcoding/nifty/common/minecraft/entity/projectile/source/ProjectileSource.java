package net.netcoding.nifty.common.minecraft.entity.projectile.source;

import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.core.util.misc.Vector;

/**
 * Represents a valid source of a projectile.
 */
public interface ProjectileSource {

	/**
	 * Launches a {@link Projectile} from the ProjectileSource.
	 *
	 * @param <T> A subclass of {@link Projectile}.
	 * @param projectile Class of the projectile to launch.
	 * @return The launched projectile.
	 */
	default <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		return this.launchProjectile(projectile, null);
	}

	/**
	 * Launches a {@link Projectile} from the ProjectileSource with an
	 * initial velocity.
	 *
	 * @param <T> A subclass of {@link Projectile}.
	 * @param projectile Class of the projectile to launch.
	 * @param velocity The velocity with which to launch.
	 * @return The launched projectile.
	 */
	<T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity);

}