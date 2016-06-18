package net.netcoding.niftybukkit._new_.minecraft.entity;

import net.netcoding.niftybukkit._new_.minecraft.source.projectiles.ProjectileSource;

public interface Projectile {

	boolean doesBounce();

	ProjectileSource getShooter();

	void setBounce(boolean value);

	void setShooter(ProjectileSource shooter);

}