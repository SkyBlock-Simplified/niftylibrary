package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.Bukkit;

public class Location extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		return String.format("%s,%d,%d,%d:%.2f,%.2f", location.getWorld().getName(), location.getBlockX(), location.getBlockX(), location.getBlockZ(), location.getPitch(), location.getYaw());
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		String[] locationArr = ((String)section).split(":");
		//1408, unit d, the orange line

		if (locationArr.length >= 1) {
			String[] locationStr = locationArr[0].split(",");

			if (locationStr.length >= 3) {
				String world = locationStr.length == 3 ? "world" : locationStr[0];
				int index = locationStr.length == 3 ? 0 : 1;
				org.bukkit.Location location = new org.bukkit.Location(Bukkit.getWorld(world), new Integer(locationStr[index]), new Integer(locationStr[index + 1]), new Integer(locationStr[index + 2]));

				if (locationArr.length == 2) {
					String[] extraStr = locationArr[1].split(",");
					location.setPitch(new Float(extraStr[0]));
					location.setYaw(new Float(extraStr[1]));
				}

				return location;
			}
		}

		throw new InvalidConfigurationException(String.format("Unable to parse location %s!", type.getName()));
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.Location.class.isAssignableFrom(type);
	}

}