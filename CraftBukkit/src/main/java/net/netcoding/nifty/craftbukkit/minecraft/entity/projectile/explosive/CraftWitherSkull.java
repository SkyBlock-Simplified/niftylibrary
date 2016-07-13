package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.explosive;

import net.netcoding.nifty.common.minecraft.entity.projectile.explosive.WitherSkull;

public final class CraftWitherSkull extends CraftFireball implements WitherSkull {

	public CraftWitherSkull(org.bukkit.entity.WitherSkull witherSkull) {
		super(witherSkull);
	}

	@Override
	public boolean isCharged() {
		return this.getHandle().isCharged();
	}

	@Override
	public org.bukkit.entity.WitherSkull getHandle() {
		return (org.bukkit.entity.WitherSkull)super.getHandle();
	}

	@Override
	public void setCharged(boolean charged) {
		this.getHandle().setCharged(charged);
	}

}