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
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		org.bukkit.block.Block block = (org.bukkit.block.Block) obj;
		Converter locationConverter = InternalConverter.getConverter(org.bukkit.Location.class);
		java.util.Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("id", block.getType() + ((block.getData() > 0) ? ":" + block.getData() : ""));
		saveMap.put("location", locationConverter.toConfig(org.bukkit.Location.class, block.getLocation(), null));
		return saveMap;
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		java.util.Map<String, Object> blockMap = (java.util.Map<String, Object>)((ConfigSection)obj).getRawMap();
		java.util.Map<String, Object> locationMap = (java.util.Map<String, Object>)((ConfigSection)blockMap.get("location")).getRawMap();
		org.bukkit.Location location = new org.bukkit.Location(Bukkit.getWorld((String)locationMap.get("world")), (Double)locationMap.get("x"), (Double)locationMap.get("y"), (Double)locationMap.get("z"));
		org.bukkit.block.Block block = location.getBlock();
		ItemStack stack = NiftyBukkit.getItemDatabase().get((String)blockMap.get("id"));
		block.setType(stack.getType());

		return block;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.block.Block.class.isAssignableFrom(type);
	}

}