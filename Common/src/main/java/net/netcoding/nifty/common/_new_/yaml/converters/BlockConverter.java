package net.netcoding.nifty.common._new_.yaml.converters;

import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "deprecation" })
public final class BlockConverter extends Converter {

	public BlockConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> blockMap = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		Converter locationConverter = this.getConverter(net.netcoding.nifty.common._new_.minecraft.region.Location.class);
		Location location = (Location)locationConverter.fromConfig(Location.class, blockMap.get("location"), null);
		net.netcoding.nifty.common._new_.minecraft.block.Block block = location.getBlock();
		net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack stack = Nifty.getItemDatabase().get((String)blockMap.get("id"));
		block.setType(stack.getType());

		return block;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		net.netcoding.nifty.common._new_.minecraft.block.Block block = (net.netcoding.nifty.common._new_.minecraft.block.Block) obj;
		Converter locationConverter = this.getConverter(Location.class);
		ConcurrentLinkedMap<String, Object> saveMap = new ConcurrentLinkedMap<>();
		saveMap.put("id", block.getType() + ((block.getData() > 0) ? ":" + block.getData() : ""));
		saveMap.put("location", locationConverter.toConfig(Location.class, block.getLocation(), null));
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.nifty.common._new_.minecraft.block.Block.class.isAssignableFrom(type);
	}

}