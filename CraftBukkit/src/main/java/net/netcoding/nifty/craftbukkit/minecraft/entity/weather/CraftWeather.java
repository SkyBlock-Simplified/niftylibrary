package net.netcoding.nifty.craftbukkit.minecraft.entity.weather;

import net.netcoding.nifty.common.minecraft.entity.weather.Weather;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public class CraftWeather extends CraftEntity implements Weather {

	public CraftWeather(org.bukkit.entity.Weather weather) {
		super(weather);
	}

	@Override
	public org.bukkit.entity.Weather getHandle() {
		return (org.bukkit.entity.Weather)super.getHandle();
	}

}