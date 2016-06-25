package net.netcoding.nifty.common._new_.minecraft.entity.projectile;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.minecraft.entity.projectile.source.ProjectileSource;

public interface Projectile extends Entity {

	boolean doesBounce();

	ProjectileSource getShooter();

	void setBounce(boolean value);

	void setShooter(ProjectileSource shooter);

}