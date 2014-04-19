package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

import org.bukkit.Bukkit;

@SuppressWarnings("unchecked")
public class Location extends Converter {

	public Location(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> locationMap = (java.util.Map<String, Object>)((ConfigSection)obj).getRawMap();
		return new org.bukkit.Location(Bukkit.getWorld((String)locationMap.get("world")), (Double)locationMap.get("x"), (Double)locationMap.get("y"), (Double)locationMap.get("z"));
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		java.util.Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("world", location.getWorld().getName());
		saveMap.put("x", location.getX());
		saveMap.put("y", location.getY());
		saveMap.put("z", location.getZ());

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.Location.class.isAssignableFrom(type);
	}

}