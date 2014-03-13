package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.InternalConverter;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({ "unchecked", "deprecation" })
public class Block extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.block.Block block = (org.bukkit.block.Block)obj;
		java.util.Map<String, Object> blockInfo = new HashMap<>();
		Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);
		blockInfo.put("id", block.getType().name() + (block.getTypeId() == 0 ? "" : ":" + block.getTypeId()));
		blockInfo.put("location", converter.toConfig((Class<?>)genericType.getActualTypeArguments()[0], block.getLocation(), null));
		return blockInfo;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> blockInfo = (java.util.Map<String, Object>)section;
		Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);
		org.bukkit.Location location = (org.bukkit.Location)converter.fromConfig((Class<?>)genericType.getActualTypeArguments()[0], blockInfo.get("location"), null);
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