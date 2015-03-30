package net.netcoding.niftybukkit.inventory.enchantments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.NumberUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentDatabase extends BukkitHelper {

	private final transient File enchantmentsFile;
	private final transient Map<String, Integer> enchantments = new HashMap<>();
	final transient Map<EnchantmentData, List<String>> names = new HashMap<>();
	final transient Map<EnchantmentData, String> primaryName = new HashMap<>();

	public EnchantmentDatabase(JavaPlugin plugin) {
		this(plugin, "enchantments.csv");
	}

	public EnchantmentDatabase(JavaPlugin plugin, String fileName) {
		super(plugin);
		this.enchantmentsFile = new File(this.getPlugin().getDataFolder(), fileName);
	}

	public EnchantmentData get(final Enchantment enchantment) throws RuntimeException {
		if (enchantment == null) throw new NullPointerException("The value for enchantment cannot be null!");
		return this.get(enchantment.getName());
	}

	public EnchantmentData get(final String name) throws RuntimeException {
		if (StringUtil.isEmpty(name)) throw new NullPointerException("The value for name cannot be null!");

		for (EnchantmentData enchData : names.keySet()) {
			for (String compare : enchData.getNames()) {
				if (compare.equalsIgnoreCase(name))
					return new EnchantmentData(enchData.getEnchantment());
			}
		}

		for (EnchantmentData enchData : names.keySet()) {
			for (String compare : enchData.getNames()) {
				if (compare.startsWith(name.toLowerCase()))
					return new EnchantmentData(enchData.getEnchantment());
			}
		}

		return null;
	}

	public List<String> names(Enchantment enchantment) {
		List<String> nameList = this.names.get(enchantment);
		if (nameList.size() > 15) nameList = nameList.subList(0, 14);
		return nameList;
	}

	public String name(Enchantment enchantment) {
		return this.primaryName.get(enchantment);
	}

	public List<EnchantmentData> parse(String[] enchList) throws NumberFormatException {
		if (ListUtil.isEmpty(enchList)) return Collections.emptyList();
		List<EnchantmentData> enchDataList = new ArrayList<>();

		for (int i = 0; i < enchList.length; i++) {
			String ench = enchList[i];

			if (ench.contains(":")) {
				String[] split = ench.split(":");
				EnchantmentData enchData = this.get(split[0]);
				String enchLevel = split[1];

				if (NumberUtil.isInt(enchLevel)) {
					enchData.setUserLevel(Short.parseShort(enchLevel));
					try { enchDataList.add(enchData); } catch (Exception ex) { }
				} else
					throw new NumberFormatException("Data value must be an integer, list or range!");
			} else {
				EnchantmentData enchData = this.get(ench);

				if (NumberUtil.isInt(enchList[i + 1]))
					enchData.setUserLevel(Short.parseShort(enchList[++i]));

				try { enchDataList.add(enchData); } catch (Exception ex) { }
			}
		}

		return Collections.unmodifiableList(enchDataList);
	}

	public List<EnchantmentData> parse(String enchStringList) throws NumberFormatException {
		if (StringUtil.isEmpty(enchStringList)) return Collections.emptyList();
		return this.parse(enchStringList.split("[,\\s]+"));
	}

	public List<String> getLines() {
		try {
			try (InputStreamReader inputStream = (this.enchantmentsFile.exists() ? new FileReader(this.enchantmentsFile) : new InputStreamReader(this.getPlugin().getResource(this.enchantmentsFile.getName())))) {
				try (BufferedReader reader = new BufferedReader(inputStream)) {
					List<String> lines = new ArrayList<>();

					do {
						String line = reader.readLine();
						if (StringUtil.isEmpty(line)) break;
						lines.add(line);
					} while (true);

					return Collections.unmodifiableList(lines);
				}
			}
		} catch (IOException ex) {
			this.getLog().console(ex);
			return Collections.emptyList();
		}
	}

	public void save() {
		this.save(false);
	}

	public void save(boolean replace) {
		this.getPlugin().saveResource(this.enchantmentsFile.getName(), replace);
	}

	public void reload() {
		final List<String> lines = this.getLines();
		if (lines.isEmpty()) return;

		this.names.clear();
		this.primaryName.clear();
		LengthCompare compare = new LengthCompare();

		for (String line : lines) {
			line = line.trim().toLowerCase(Locale.ENGLISH);
			if (line.length() > 0 && line.charAt(0) == '#') continue;
			final String[] parts = line.split(",");
			if (parts.length < 3) continue;
			String itemName = parts[0].toLowerCase(Locale.ENGLISH);
			final int numeric = Integer.parseInt(parts[1]);
			this.enchantments.put(itemName, numeric);
			EnchantmentData enchData = new EnchantmentData(Enchantment.getById(numeric));

			if (!this.names.containsKey(enchData)) {
				this.names.put(enchData, new ArrayList<>(Arrays.asList(itemName)));
				this.primaryName.put(enchData, itemName);
			} else {
				this.names.get(enchData).add(itemName);
				Collections.sort(this.names.get(enchData), compare);
			}
		}
	}

	private class LengthCompare implements Comparator<String> {

		public LengthCompare() {
			super();
		}

		@Override
		public int compare(String s1, String s2) {
			return s1.length() - s2.length();
		}

	}

}