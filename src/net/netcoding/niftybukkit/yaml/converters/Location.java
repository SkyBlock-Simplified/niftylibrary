package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import org.bukkit.Bukkit;

public class Location extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		return String.format("%s,%.2f,%.2f,%.2f", location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		String[] location = ((String)section).split(",");
		return new org.bukkit.Location(Bukkit.getWorld(location[0]), new Double(location[1]), new Double(location[2]), new Double(location[3]));
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.Location.class.isAssignableFrom(type);
	}

}