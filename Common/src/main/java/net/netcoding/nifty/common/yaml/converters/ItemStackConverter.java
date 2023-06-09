package net.netcoding.nifty.common.yaml.converters;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.ItemMeta;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.inventory.ItemFlag;
import net.netcoding.nifty.common.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ItemStackConverter extends Converter {

	public ItemStackConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Converter mapConverter = this.getConverter(Map.class);
		Map<String, Object> itemMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, section, null);
		Map<String, Object> metaMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, itemMap.get("meta"), null);
		net.netcoding.nifty.common.minecraft.inventory.item.ItemStack item = Nifty.getItemDatabase().get((String)itemMap.get("id"));
		item.setAmount((int)itemMap.get("amount"));
		ItemMeta meta = item.getItemMeta();

		if (metaMap != null) {
			if (metaMap.containsKey("name"))
				meta.setDisplayName((String)metaMap.get("name"));

			if (metaMap.containsKey("lore"))
				meta.setLore((List<String>)this.getConverter(List.class).fromConfig(List.class, metaMap.get("lore"), null));

			if (MinecraftProtocol.isPost1_7()) {
				if (metaMap.containsKey("flags")) {
					ParameterizedType flagType = (ParameterizedType)ItemFlag.class.getGenericSuperclass();
					Set<ItemFlag> itemFlags = (Set<ItemFlag>)this.getConverter(Set.class).fromConfig(Set.class, metaMap.get("flags"), flagType);
					meta.addItemFlags(ListUtil.toArray(itemFlags, ItemFlag.class));
				}
			}

			if (metaMap.containsKey("enchantments")) {
				Map<String, Integer> enchMap = (Map<String, Integer>)mapConverter.fromConfig(HashMap.class, metaMap.get("enchantments"), null);

				for (Map.Entry<String, Integer> enchantment : enchMap.entrySet())
					meta.addEnchant(Enchantment.getByName(enchantment.getKey()), enchantment.getValue(), true);
			}
		}

		if (itemMap.containsKey("nbt")) {
			Map<String, Object> nbtMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, itemMap.get("nbt"), null);
			item.getNbt().putAll(nbtMap);
		}

		item.setItemMeta(meta);
		return item;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		net.netcoding.nifty.common.minecraft.inventory.item.ItemStack item = (net.netcoding.nifty.common.minecraft.inventory.item.ItemStack)obj;
		ItemMeta meta = item.getItemMeta(true);
		ConcurrentLinkedMap<String, Object> saveMap = Concurrent.newLinkedMap();
		ConcurrentLinkedMap<String, Object> metaMap = Concurrent.newLinkedMap();
		List<String> lore = new ArrayList<>();
		saveMap.put("id", item.getType() + ((item.getDurability() > 0) ? ":" + item.getDurability() : ""));
		saveMap.put("amount", item.getAmount());
		metaMap.put("name", meta.hasDisplayName() ? meta.getDisplayName() : "");

		if (meta.hasLore())
			lore.addAll(meta.getLore());

		metaMap.put("lore", this.getConverter(List.class).toConfig(List.class, lore, null));

		if (MinecraftProtocol.isPost1_7()) {
			ParameterizedType flagType = (ParameterizedType)ItemFlag.class.getGenericSuperclass();

			if (!meta.getItemFlags().isEmpty())
				metaMap.put("flags", this.getConverter(Set.class).toConfig(Set.class, meta.getItemFlags(), flagType));
		}

		if (!meta.getEnchants().isEmpty()) {
			ConcurrentLinkedMap<String, Integer> enchantments = Concurrent.newLinkedMap();

			for (Map.Entry<Enchantment, Integer> enchantment : meta.getEnchants().entrySet())
				enchantments.put(enchantment.getKey().getName(), enchantment.getValue());

			metaMap.put("enchantments", this.getConverter(Map.class).toConfig(Map.class, enchantments, null));
		}

		saveMap.put("meta", metaMap);

		if (item.getNbt().notEmpty())
			saveMap.put("nbt", this.getConverter(Map.class).toConfig(Map.class, item.getNbt(), null));

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.nifty.common.minecraft.inventory.item.ItemStack.class.isAssignableFrom(type);
	}

}