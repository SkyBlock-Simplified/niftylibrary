package net.netcoding.nifty.craftbukkit.yaml.converters;

import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;
import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemDatabase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "deprecation" })
public final class CraftBlockConverter extends Converter {

	public CraftBlockConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> blockMap = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		Location location = (org.bukkit.Location)this.getConverter(Location.class).fromConfig(Location.class, blockMap.get("location"), null);
		Block block = location.getBlock();
		ItemStack stack = CraftItemDatabase.getInstance().getBukkit((String)blockMap.get("id"));
		block.setType(stack.getType());
		return block;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.block.Block block = (org.bukkit.block.Block) obj;
		Converter locationConverter = this.getConverter(Location.class);
		ConcurrentLinkedMap<String, Object> saveMap = Concurrent.newLinkedMap();
		saveMap.put("id", block.getType() + ((block.getData() > 0) ? ":" + block.getData() : ""));
		saveMap.put("location", locationConverter.toConfig(Location.class, block.getLocation(), null));
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.block.Block.class.isAssignableFrom(type);
	}

}