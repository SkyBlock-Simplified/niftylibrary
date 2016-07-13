package net.netcoding.nifty.craftbukkit.minecraft.entity.projectile.arrow;

import net.netcoding.nifty.common.minecraft.entity.projectile.arrow.SpectralArrow;

public final class CraftSpectralArrow extends CraftArrow implements SpectralArrow {

	public CraftSpectralArrow(org.bukkit.entity.SpectralArrow spectralArrow) {
		super(spectralArrow);
	}

	@Override
	public int getGlowingTicks() {
		return this.getHandle().getGlowingTicks();
	}

	@Override
	public org.bukkit.entity.SpectralArrow getHandle() {
		return (org.bukkit.entity.SpectralArrow)super.getHandle();
	}

	@Override
	public void setGlowingTicks(int duration) {
		this.getHandle().setGlowingTicks(duration);
	}

}