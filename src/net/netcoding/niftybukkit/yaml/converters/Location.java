package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Location extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		return String.format("%.2f,%.2f,%.2f,%s", location.getX(), location.getY(), location.getZ(), location.getWorld().getName());
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		String[] location = ((String)section).split(",");
		double x = new Double(location[0]).doubleValue();
		double y = new Double(location[1]).doubleValue();
		double z = new Double(location[2]).doubleValue();
		World w  = Bukkit.getWorld(location[3]);
		return new org.bukkit.Location(w, x, y, z);
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.Set.class.isAssignableFrom(type);
	}

}