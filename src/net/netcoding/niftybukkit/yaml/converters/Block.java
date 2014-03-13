package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({ "unchecked", "deprecation" })
public class Block extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.block.Block block = (org.bukkit.block.Block) obj;
		Converter locationConverter = InternalConverter.getConverter(org.bukkit.Location.class);
		java.util.Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("id", block.getType() + ((block.getData() > 0) ? ":" + block.getData() : ""));
		saveMap.put("location", locationConverter.toConfig(org.bukkit.Location.class, block.getLocation(), null));

		return saveMap;
	}

	public Object fromConfig2(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> blockMap = (java.util.Map<String, Object>)((ConfigSection)section).getRawMap();
		java.util.Map<String, Object> locationMap = (java.util.Map<String, Object>)((ConfigSection)blockMap.get("location")).getRawMap();
		org.bukkit.Location location = new org.bukkit.Location(Bukkit.getWorld((String)locationMap.get("world")), (Double)locationMap.get("x"), (Double)locationMap.get("y"), (Double)locationMap.get("z"));
		org.bukkit.block.Block block = location.getBlock();
		ItemStack stack = NiftyBukkit.getItemDatabase().get((String)blockMap.get("id"));
		block.setType(stack.getType());

		return block;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> blockInfo = (java.util.Map<String, Object>)section;
		Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.converters.Location.class);
		org.bukkit.Location location = (org.bukkit.Location)converter.fromConfig(org.bukkit.Location.class, blockInfo.get("location"), null);
		org.bukkit.block.Block block = Bukkit.getServer().getWorld(location.getWorld().getName()).getBlockAt(location);
		ItemStack itemData = NiftyBukkit.getItemDatabase().get((String)blockInfo.get("id"));
		block.setType(itemData.getType());
		return block;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.block.Block.class.isAssignableFrom(type);
	}

}