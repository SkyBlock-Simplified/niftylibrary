package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.inventory.items.ItemData;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.yaml.ConfigSection;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ItemStack extends Converter {

	public ItemStack(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> itemMap = (section instanceof Map ? (Map<String, Object>)section : (Map<String, Object>)((ConfigSection)section).getRawMap());
		Map<String, Object> metaMap = (Map<String, Object>)(itemMap.get("meta") instanceof Map ? itemMap.get("meta") : ((ConfigSection)itemMap.get("meta")).getRawMap());
		ItemData itemData = new ItemData(NiftyBukkit.getItemDatabase().get((String)itemMap.get("id")));
		itemData.setAmount((int)itemMap.get("amount"));
		ItemMeta meta = itemData.getItemMeta();

		if (metaMap != null) {
			if (metaMap.get("name") != null) {
				String name = RegexUtil.replaceColor((String)metaMap.get("name"), RegexUtil.REPLACE_ALL_PATTERN);
				meta.setDisplayName(StringUtil.notEmpty(name) ? name : null);
			}

			if (metaMap.get("lore") != null) {
				List<String> lore = (List<String>)this.getConverter(List.class).fromConfig(List.class, metaMap.get("lore"), null);

				for (int i = 0; i < lore.size(); i++)
					lore.set(i, RegexUtil.replaceColor(lore.get(i), RegexUtil.REPLACE_ALL_PATTERN));

				meta.setLore(lore);
			}
		}

		itemData.setItemMeta(meta);
		return itemData;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.inventory.ItemStack itemStack = (org.bukkit.inventory.ItemStack)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("id", itemStack.getType() + ((itemStack.getDurability() > 0) ? ":" + itemStack.getDurability() : ""));
		saveMap.put("amount", itemStack.getAmount());
		Converter listConverter = this.getConverter(List.class);
		Map<String, Object> meta = new HashMap<>();
		meta.put("name", itemStack.getItemMeta().hasDisplayName() ? RegexUtil.replace(itemStack.getItemMeta().getDisplayName(), RegexUtil.VANILLA_PATTERN, "&$1") : "");
		List<String> lore = new ArrayList<>();

		if (itemStack.getItemMeta().hasLore()) {
			lore.addAll(itemStack.getItemMeta().getLore());

			for (int i = 0; i < lore.size(); i++)
				lore.set(i, RegexUtil.replace(lore.get(i), RegexUtil.VANILLA_PATTERN, "&$1"));
		}

		meta.put("lore", listConverter.toConfig(List.class, lore, null));

		saveMap.put("meta", meta);
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}