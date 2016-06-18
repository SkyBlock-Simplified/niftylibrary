package net.netcoding.nifty.craftbukkit.yaml.converters;

import com.google.common.base.Preconditions;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;
import net.netcoding.niftycore.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CraftLocation extends Converter {

	private final static ConcurrentSet<Location> WILDCARDS = new ConcurrentSet<>();

	public CraftLocation(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		float yaw = (map.get("yaw") instanceof Double) ? ((Double)map.get("yaw")).floatValue() : (Float)map.get("yaw");
		float pitch = (map.get("pitch") instanceof Double) ? ((Double)map.get("pitch")).floatValue() : (Float)map.get("pitch");
		String worldName = (String)map.get("world");
		Preconditions.checkArgument(worldName != null, "World cannot be NULL!");
		World world;
		boolean wildcard = false;

		if (worldName.matches("(?i)^%(world%)?$")) {
			world = Bukkit.getServer().getWorlds().get(0);
			wildcard = true;
		} else
			world = Bukkit.getServer().getWorld(worldName);

		Location location = new Location(world, (Double)map.get("x"), (Double)map.get("y"), (Double)map.get("z"), yaw, pitch);

		if (wildcard)
			WILDCARDS.add(location);

		return location;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.Location location = (org.bukkit.Location)obj;
		ConcurrentLinkedMap<String, Object> saveMap = new ConcurrentLinkedMap<>();
		saveMap.put("world", (WILDCARDS.contains(location) ? "%world%" : location.getWorld().getName()));
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