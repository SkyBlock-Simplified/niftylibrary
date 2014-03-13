package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Location extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		return String.format("%s,%.2f,%.2f,%.2f", location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		String[] location = ((String)section).split(",");
		World w  = Bukkit.getWorld(location[0]);
		double x = new Double(location[1]).doubleValue();
		double y = new Double(location[2]).doubleValue();
		double z = new Double(location[3]).doubleValue();
		return new org.bukkit.Location(w, x, y, z);
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.Location.class.isAssignableFrom(type);
	}

}