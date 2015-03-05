package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

import org.bukkit.inventory.ItemStack;

@SuppressWarnings({ "unchecked", "deprecation" })
public class Block extends Converter {

	public Block(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> blockMap = (java.util.Map<String, Object>)(section instanceof java.util.Map ? (java.util.Map<String, Object>)section : ((ConfigSection)section).getRawMap());
		org.bukkit.Location location = (org.bukkit.Location)this.getConverter(org.bukkit.Location.class).fromConfig(org.bukkit.Location.class, blockMap.get("location"), null);
		org.bukkit.block.Block block = location.getBlock();
		ItemStack stack = NiftyBukkit.getItemDatabase().get((String)blockMap.get("id"));
		block.setType(stack.getType());

		return block;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.block.Block block = (org.bukkit.block.Block) obj;
		Converter locationConverter = this.getConverter(org.bukkit.Location.class);
		java.util.Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("id", block.getType() + ((block.getData() > 0) ? ":" + block.getData() : ""));
		saveMap.put("location", locationConverter.toConfig(org.bukkit.Location.class, block.getLocation(), null));
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.block.Block.class.isAssignableFrom(type);
	}

}