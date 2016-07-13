package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.ProjectileSource;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

public abstract class CraftProjectile extends CraftEntity implements Projectile {

	protected CraftProjectile(org.bukkit.entity.Projectile projectile) {
		super(projectile);
	}

	@Override
	public boolean doesBounce() {
		return this.getHandle().doesBounce();
	}

	@Override
	public org.bukkit.entity.Projectile getHandle() {
		return (org.bukkit.entity.Projectile)super.getHandle();
	}

	@Override
	public ProjectileSource getShooter() {
		return CraftConverter.fromBukkitSource(this.getHandle().getShooter());
	}

	@Override
	public void setBounce(boolean value) {
		this.getHandle().setBounce(value);
	}

	@Override
	public void setShooter(ProjectileSource shooter) {
		this.getHandle().setShooter(CraftConverter.toBukkitSource(shooter));
	}

}