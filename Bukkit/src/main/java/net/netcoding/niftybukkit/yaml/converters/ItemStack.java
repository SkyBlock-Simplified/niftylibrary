package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.minecraft.items.enchantments.EnchantmentData;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
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
		Converter mapConverter = this.getConverter(Map.class);
		Map<String, Object> itemMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, section, null);
		Map<String, Object> metaMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, itemMap.get("meta"), null);
		ItemData itemData = NiftyBukkit.getItemDatabase().get((String)itemMap.get("id"));
		itemData.setAmount((int)itemMap.get("amount"));
		ItemMeta itemMeta = itemData.getItemMeta();

		if (metaMap != null) {
			if (metaMap.containsKey("name"))
				itemMeta.setDisplayName((String)metaMap.get("name"));

			if (metaMap.containsKey("lore"))
				itemMeta.setLore((List<String>)this.getConverter(List.class).fromConfig(List.class, metaMap.get("lore"), null));

			if (MinecraftProtocol.isPost1_7()) {
				if (metaMap.containsKey("flags")) {
					ParameterizedType flagType = (ParameterizedType)org.bukkit.inventory.ItemFlag.class.getGenericSuperclass();
					Set<org.bukkit.inventory.ItemFlag> itemFlags = (Set<org.bukkit.inventory.ItemFlag>)this.getConverter(Set.class).fromConfig(Set.class, metaMap.get("flags"), flagType);
					itemMeta.addItemFlags(ListUtil.toArray(itemFlags, org.bukkit.inventory.ItemFlag.class));
				}
			}

			if (metaMap.containsKey("enchantments")) {
				Map<String, Integer> enchMap = (Map<String, Integer>)mapConverter.fromConfig(HashMap.class, metaMap.get("enchantments"), null);

				for (Map.Entry<String, Integer> enchantment : enchMap.entrySet()) {
					EnchantmentData enchantmentData = new EnchantmentData(Enchantment.getByName(enchantment.getKey()));
					enchantmentData.setUserLevel(enchantment.getValue());
					itemMeta.addEnchant(enchantmentData.getEnchantment(), enchantmentData.getUserLevel(), true);
				}
			}
		}

		if (itemMap.containsKey("nbt")) {
			Map<String, Object> nbtMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, itemMap.get("nbt"), null);
			itemData.getNbt().putAll(nbtMap);
		}

		itemData.setItemMeta(itemMeta);
		return itemData;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		ItemData itemData = new ItemData((org.bukkit.inventory.ItemStack)obj);
		ItemMeta itemMeta = itemData.getItemMeta(true);
		ConcurrentLinkedMap<String, Object> saveMap = new ConcurrentLinkedMap<>();
		ConcurrentLinkedMap<String, Object> meta = new ConcurrentLinkedMap<>();
		List<String> lore = new ArrayList<>();
		saveMap.put("id", itemData.getType() + ((itemData.getDurability() > 0) ? ":" + itemData.getDurability() : ""));
		saveMap.put("amount", itemData.getAmount());
		meta.put("name", itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "");

		if (itemMeta.hasLore())
			lore.addAll(itemMeta.getLore());

		meta.put("lore", this.getConverter(List.class).toConfig(List.class, lore, null));

		if (MinecraftProtocol.isPost1_7()) {
			ParameterizedType flagType = (ParameterizedType)org.bukkit.inventory.ItemFlag.class.getGenericSuperclass();

			if (!itemMeta.getItemFlags().isEmpty())
				meta.put("flags", this.getConverter(Set.class).toConfig(Set.class, itemMeta.getItemFlags(), flagType));
		}

		if (!itemMeta.getEnchants().isEmpty()) {
			ConcurrentLinkedMap<String, Integer> enchantments = new ConcurrentLinkedMap<>();

			for (Map.Entry<Enchantment, Integer> enchantment : itemMeta.getEnchants().entrySet())
				enchantments.put(enchantment.getKey().getName(), enchantment.getValue());

			meta.put("enchantments", this.getConverter(Map.class).toConfig(Map.class, enchantments, null));
		}

		saveMap.put("meta", meta);

		if (itemData.getNbt().notEmpty())
			saveMap.put("nbt", this.getConverter(Map.class).toConfig(Map.class, itemData.getNbt(), null));

		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.inventory.ItemStack.class.isAssignableFrom(type);
	}

}