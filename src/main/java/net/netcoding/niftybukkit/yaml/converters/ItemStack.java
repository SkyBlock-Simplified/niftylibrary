package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.minecraft.nbt.NbtFactory;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.yaml.ConfigSection;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ItemStack extends Converter {

	public ItemStack(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> itemMap = (section instanceof Map ? (Map<String, Object>)section : (Map<String, Object>)((ConfigSection)section).getRawMap());
		Map<String, Object> metaMap = (itemMap.get("meta") instanceof Map ? (Map<String, Object>)itemMap.get("meta") : (Map<String, Object>)((ConfigSection)itemMap.get("meta")).getRawMap());
		Map<String, Integer> enchMap = (itemMap.get("enchantments") instanceof Map ? (Map<String, Integer>)itemMap.get("enchantments") : (Map<String, Integer>)((ConfigSection)itemMap.get("enchantments")).getRawMap());
		ItemData itemData = new ItemData(NiftyBukkit.getItemDatabase().get((String)itemMap.get("id")));
		itemData.setAmount((int)itemMap.get("amount"));
		ItemMeta itemMeta = itemData.getItemMeta();

		if (metaMap != null) {
			if (metaMap.containsKey("name"))
				itemMeta.setDisplayName((String)metaMap.get("name"));

			if (metaMap.containsKey("lore"))
				itemMeta.setLore((List<String>)this.getConverter(List.class).fromConfig(List.class, metaMap.get("lore"), null));

			if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_8_pre1.getProtocol()) {
				if (metaMap.containsKey("flags")) {
					Converter setConverter = this.getConverter(Set.class);
					ParameterizedType flagType = (ParameterizedType)org.bukkit.inventory.ItemFlag.class.getGenericSuperclass();
					Set<org.bukkit.inventory.ItemFlag> itemFlags = (Set<org.bukkit.inventory.ItemFlag>)setConverter.fromConfig(Set.class, metaMap.get("flags"), flagType);
					itemMeta.addItemFlags(ListUtil.toArray(itemFlags, org.bukkit.inventory.ItemFlag.class));
				}
			}
		}

		for (Map.Entry<String, Integer> enchantment : enchMap.entrySet())
			itemData.addUnsafeEnchantment(Enchantment.getByName(enchantment.getKey()), enchantment.getValue());

		if (itemMap.containsKey("nbt")) {
			Converter mapConverter = this.getConverter(Map.class);
			itemData.putAllNbt((Map<String, Object>)mapConverter.fromConfig(Map.class, itemMap.get("nbt"), null));
		}

		itemData.setItemMeta(itemMeta);
		return itemData;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		ItemData itemData = new ItemData((org.bukkit.inventory.ItemStack)obj);
		ItemMeta itemMeta = itemData.getItemMeta();
		Converter listConverter = this.getConverter(List.class);
		LinkedHashMap<String, Object> saveMap = new LinkedHashMap<>();
		LinkedHashMap<String, Object> meta = new LinkedHashMap<>();
		LinkedHashMap<String, Integer> enchantments = new LinkedHashMap<>();
		List<String> lore = new ArrayList<>();
		saveMap.put("id", itemData.getType() + ((itemData.getDurability() > 0) ? ":" + itemData.getDurability() : ""));
		saveMap.put("amount", itemData.getAmount());
		meta.put("name", itemMeta.hasDisplayName() ? RegexUtil.replace(itemMeta.getDisplayName(), RegexUtil.VANILLA_PATTERN, "&$1") : "");

		if (itemMeta.hasLore()) {
			lore.addAll(itemMeta.getLore());

			for (int i = 0; i < lore.size(); i++)
				lore.set(i, RegexUtil.replace(lore.get(i), RegexUtil.VANILLA_PATTERN, "&$1"));
		}

		meta.put("lore", listConverter.toConfig(List.class, lore, null));

		for (Map.Entry<Enchantment, Integer> enchantment : itemData.getEnchantments().entrySet())
			enchantments.put(enchantment.getKey().getName(), enchantment.getValue());

		if (MinecraftProtocol.getCurrentProtocol() >= MinecraftProtocol.v1_8_pre1.getProtocol()) {
			Converter setConverter = this.getConverter(Set.class);
			ParameterizedType flagType = (ParameterizedType)org.bukkit.inventory.ItemFlag.class.getGenericSuperclass();
			meta.put("flags", setConverter.toConfig(Set.class, itemMeta.getItemFlags(), flagType));
		}

		saveMap.put("meta", meta);
		saveMap.put("enchantments", enchantments);

		if (itemData.containsNbt())
			saveMap.put("nbt", NbtFactory.fromItemTag(itemData));

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}