package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftycore.yaml.ConfigSection;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.Bukkit;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Location extends Converter {

	public Location(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)((section instanceof Map) ? section : ((ConfigSection)section).getRawMap());
		float yaw = (map.get("yaw") instanceof Double) ? ((Double)map.get("yaw")).floatValue() : (Float)map.get("yaw");
		float pitch = (map.get("pitch") instanceof Double) ? ((Double)map.get("pitch")).floatValue() : (Float)map.get("pitch");
		return new org.bukkit.Location(Bukkit.getWorld((String)map.get("world")), (Double)map.get("x"), (Double)map.get("y"), (Double)map.get("z"), yaw, pitch);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("world", location.getWorld().getName());
		saveMap.put("x", location.getX());
		saveMap.put("y", location.getY());
		saveMap.put("z", location.getZ());
		saveMap.put("yaw", location.getYaw());
		saveMap.put("pitch", location.getPitch());

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.Location.class.isAssignableFrom(type);
	}

}