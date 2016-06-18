package net.netcoding.niftybukkit._new_.api.inventory.item.enchantment;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.enchantment.Enchantment;
import net.netcoding.niftycore.util.misc.CSVStorage;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.NumberUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.comparator.LengthCompare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EnchantmentDatabase extends CSVStorage {

	private final transient Map<EnchantmentData, List<String>> names = new HashMap<>();
	private final transient Map<EnchantmentData, String> primaryName = new HashMap<>();

	protected EnchantmentDatabase() {
		super(Nifty.getPlugin().getPluginDescription().getStorage(), "enchantments");
		this.registerEnchantments0();
	}

	public List<EnchantmentData> getPossibleEnchants(ItemStack stack) {
		return this.names.keySet().stream().filter(enchantment -> enchantment.canEnchant(stack)).collect(Collectors.toList());
	}

	public EnchantmentData get(Enchantment enchantment) {
		if (enchantment == null)
			throw new IllegalArgumentException("Enchantment cannot be NULL!");

		return this.get(enchantment.getName());
	}

	public EnchantmentData get(String name) {
		if (StringUtil.isEmpty(name))
			throw new IllegalArgumentException("Name cannot be NULL!");

		for (Map.Entry<EnchantmentData, List<String>> entry : this.names.entrySet()) {
			for (String compare : entry.getValue()) {
				if (compare.equalsIgnoreCase(name))
					return entry.getKey();
			}
		}

		throw new IllegalArgumentException(StringUtil.format("Enchantment with name ''{0}'' not found!", name));
	}

	public String name(EnchantmentData enchantmentData) {
		return this.name(enchantmentData.getEnchantment());
	}

	public String name(Enchantment enchantment) {
		return this.primaryName.get(enchantment);
	}

	public List<String> names(EnchantmentData enchantmentData) {
		return this.names(enchantmentData.getEnchantment());
	}

	public List<String> names(EnchantmentData enchantmentData, boolean shorten) {
		return this.names(enchantmentData.getEnchantment(), shorten);
	}

	public List<String> names(Enchantment enchantment) {
		return this.names(enchantment, true);
	}

	public List<String> names(Enchantment enchantment, boolean shorten) {
		List<String> nameList = this.names.get(enchantment);

		if (nameList.size() > 15 && shorten)
			nameList = nameList.subList(0, 15);

		return Collections.unmodifiableList(nameList);
	}

	public List<EnchantmentData> parse(String[] enchList) {
		if (ListUtil.isEmpty(enchList)) return Collections.emptyList();
		List<EnchantmentData> enchDataList = new ArrayList<>();

		for (int i = 0; i < enchList.length; i++) {
			String ench = enchList[i];

			if (ench.contains(":")) {
				String[] split = ench.split(":");
				EnchantmentData enchData = this.get(split[0]);
				if (enchData == null) continue;
				String enchLevel = split[1];

				try {
					if (NumberUtil.isNumber(enchLevel)) {
						enchData.setUserLevel(Integer.parseInt(enchLevel));
						enchDataList.add(enchData);
					} else if (enchLevel.matches("^max(imum)?$"))
						enchData.setUserLevel(Short.MAX_VALUE);
				} catch (NumberFormatException nfex) {
					if (nfex.getMessage().startsWith("Value out of range"))
						enchData.setUserLevel(Short.MAX_VALUE);
				}
			} else {
				EnchantmentData enchData = this.get(ench);
				if (enchData == null) continue;

				if (i + 1 < enchList.length) {
					try {
						if (NumberUtil.isNumber(enchList[i + 1]))
							enchData.setUserLevel(Integer.parseInt(enchList[++i]));
						else if (enchList[i + 1].matches("^max(imum)?$"))
							enchData.setUserLevel(Short.MAX_VALUE);
					} catch (NumberFormatException nfex) {
						if (nfex.getMessage().startsWith("Value out of range"))
							enchData.setUserLevel(Short.MAX_VALUE);
					}
				}

				enchDataList.add(enchData);
			}
		}

		return Collections.unmodifiableList(enchDataList);
	}

	public List<EnchantmentData> parse(String enchStringList) {
		if (StringUtil.isEmpty(enchStringList)) return Collections.emptyList();
		return this.parse(enchStringList.split("[,\\s]+"));
	}

	public List<String> primaryNames() {
		return Collections.unmodifiableList(new ArrayList<>(this.primaryName.values()));
	}

	protected abstract void registerEnchantments();

	private void registerEnchantments0() {
		this.registerEnchantments();
		Enchantment.stopAcceptingRegistrations();
	}

	public void reload() {
		try {
			List<String> lines = this.getLines();
			if (lines.isEmpty()) return;

			this.names.clear();
			this.primaryName.clear();
			LengthCompare compare = new LengthCompare();

			for (String line : lines) {
				line = line.trim().toLowerCase(Locale.ENGLISH);
				if (!line.isEmpty() && line.charAt(0) == '#') continue;
				final String[] parts = line.split(",");
				if (parts.length < 2) continue;
				String enchName = parts[0].toLowerCase(Locale.ENGLISH);
				final int numeric = Integer.parseInt(parts[1]);
				EnchantmentData enchData = new EnchantmentData(Enchantment.getById(numeric));
				if (enchData.getEnchantment() == null) continue;

				if (!this.names.containsKey(enchData)) {
					this.names.put(enchData, new ArrayList<>(Collections.singletonList(enchName)));
					if (enchName.contains("_")) this.names.get(enchData).add(enchName.replace("_", ""));
					this.primaryName.put(enchData, enchName);
				} else {
					this.names.get(enchData).add(enchName);
					Collections.sort(this.names.get(enchData), compare);
				}
			}
		} catch (IOException ioex) {
			Nifty.getLog().console("Unable to read ''{0}''!", ioex, this.getLocalFile().getName());
		}
	}

}