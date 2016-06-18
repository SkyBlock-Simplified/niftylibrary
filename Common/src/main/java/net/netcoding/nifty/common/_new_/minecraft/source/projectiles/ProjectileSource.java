package net.netcoding.nifty.common._new_.minecraft.source.projectiles;

import net.netcoding.nifty.common._new_.minecraft.entity.Projectile;
import net.netcoding.niftycore.util.misc.Vector;

public interface ProjectileSource {

	<T extends Projectile> T launchProjectile(Class<? extends T> var1);

	<T extends Projectile> T launchProjectile(Class<? extends T> var1, Vector var2);

}