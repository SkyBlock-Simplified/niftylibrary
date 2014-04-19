package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class ItemStack extends Converter {

	public ItemStack(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		java.util.Map<String, Object> itemMap = (java.util.Map<String, Object>)(obj instanceof java.util.Map ? obj : ((ConfigSection)obj).getRawMap());
		java.util.Map<String, Object> metaMap = (java.util.Map<String, Object>)(itemMap.get("meta") instanceof java.util.Map ? itemMap.get("meta") : ((ConfigSection)itemMap.get("meta")).getRawMap());
		org.bukkit.inventory.ItemStack stack = NiftyBukkit.getItemDatabase().get((String)itemMap.get("id"));
		stack.setAmount((int)itemMap.get("amount"));
		if (metaMap.get("name") != null) stack.getItemMeta().setDisplayName((String)metaMap.get("name"));

		if (metaMap.get("lore") != null) {
			Converter listConverter = this.getConverter(java.util.List.class);
			stack.getItemMeta().setLore((java.util.List<String>)listConverter.fromConfig(java.util.List.class, metaMap.get("lore"), null));
		}

		return stack;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.inventory.ItemStack itemStack = (org.bukkit.inventory.ItemStack)obj;
		java.util.Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("id", itemStack.getType() + ((itemStack.getDurability() > 0) ? ":" + itemStack.getDurability() : ""));
		saveMap.put("amount", itemStack.getAmount());
		Converter listConverter = this.getConverter(java.util.List.class);
		java.util.Map<String, Object> meta = new HashMap<>();
		meta.put("name", itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : null);
		meta.put("lore", itemStack.getItemMeta().hasLore() ? listConverter.toConfig(java.util.List.class, itemStack.getItemMeta().getLore(), null) : null);
		saveMap.put("meta", meta);

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}