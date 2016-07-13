package net.netcoding.nifty.craftbukkit.minecraft.entity.weather;

import net.netcoding.nifty.common.minecraft.entity.weather.LightningStrike;

public final class CraftLightningStrike extends CraftWeather implements LightningStrike {

	public CraftLightningStrike(org.bukkit.entity.LightningStrike lightningStrike) {
		super(lightningStrike);
	}

	@Override
	public org.bukkit.entity.LightningStrike getHandle() {
		return (org.bukkit.entity.LightningStrike)super.getHandle();
	}

	@Override
	public boolean isEffect() {
		return this.getHandle().isEffect();
	}

}