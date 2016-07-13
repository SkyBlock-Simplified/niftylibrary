package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.projectile.ShulkerBullet;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftShulkerBullet extends CraftProjectile implements ShulkerBullet {

	public CraftShulkerBullet(org.bukkit.entity.ShulkerBullet shulkerBullet) {
		super(shulkerBullet);
	}

	@Override
	public org.bukkit.entity.ShulkerBullet getHandle() {
		return (org.bukkit.entity.ShulkerBullet)super.getHandle();
	}

	@Override
	public Entity getTarget() {
		return CraftEntity.convertBukkitEntity(this.getHandle().getTarget());
	}

	@Override
	public void setTarget(Entity target) {
		this.getHandle().setTarget(((CraftEntity)target).getHandle());
	}

}