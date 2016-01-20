package net.netcoding.niftybukkit.inventory.items;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.NumberUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.comparator.LengthCompare;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDatabase extends BukkitHelper {

	private final transient File itemsFile;
	private final transient Map<String, Integer> items = new HashMap<>();
	private final transient Map<ItemData, List<String>> names = new HashMap<>();
	private final transient Map<ItemData, String> primaryName = new HashMap<>();
	private final transient Map<String, Short> durabilities = new HashMap<>();
	private final static transient Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

	public ItemDatabase(JavaPlugin plugin) {
		this(plugin, "items.csv");
	}

	public ItemDatabase(JavaPlugin plugin, String fileName) {
		super(plugin);
		this.itemsFile = new File(this.getPlugin().getDataFolder(), fileName);
	}

	public ItemStack get(final String id) throws RuntimeException {
		if (StringUtil.isEmpty(id)) throw new NullPointerException("The value for id cannot be null!");
		int itemid = -1;
		String itemName = id;
		short metaData = 0;
		Matcher parts = splitPattern.matcher(id);

		if (parts.matches()) {
			itemName = parts.group(2);
			metaData = Short.parseShort(parts.group(3));
		}

		if (NumberUtil.isInt(itemName))
			itemid = Integer.parseInt(itemName);
		else if (NumberUtil.isInt(id))
			itemid = Integer.parseInt(id);
		else
			itemName = itemName.toLowerCase(Locale.ENGLISH);

		if (itemid < 0) {
			if (this.items.containsKey(itemName)) {
				itemid = this.items.get(itemName);

				if (this.durabilities.containsKey(itemName) && metaData == 0)
					metaData = this.durabilities.get(itemName);
			} else if (Material.getMaterial(itemName.toUpperCase(Locale.ENGLISH)) != null)
				itemid = Material.getMaterial(itemName.toUpperCase(Locale.ENGLISH)).getId();
			else
				throw new RuntimeException("Unknown item name: " + itemName);
		}

		if (itemid < 0) throw new RuntimeException("Unknown item name: " + itemName);
		final Material mat = Material.getMaterial(itemid);
		if (mat == null) throw new RuntimeException("Unknown item id: " + itemid);
		final ItemStack retval = new ItemStack(mat);
		retval.setAmount(mat.getMaxStackSize());
		retval.setDurability(metaData);
		return retval;
	}

	public ItemStack get(final String id, final int quantity) throws RuntimeException {
		final ItemStack retval = get(id.toLowerCase(Locale.ENGLISH));
		retval.setAmount(quantity);
		return retval;
	}

	public int getId(final String itemName) throws RuntimeException {
		return this.get(itemName).getTypeId();
	}

	public List<ItemStack> getMatching(Player player, String[] args) throws RuntimeException {
		List<ItemStack> is = new ArrayList<>();

		if (args.length < 1)
			is.add(player.getItemInHand());
		else if ("hand".equalsIgnoreCase(args[0]))
			is.add(player.getItemInHand());
		else if ("inventory".equalsIgnoreCase(args[0]) || "invent".equalsIgnoreCase(args[0]) || "all".equalsIgnoreCase(args[0])) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR) continue;
				is.add(stack);
			}
		} else if ("blocks".equalsIgnoreCase(args[0])) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR || !stack.getType().isBlock()) continue;
				is.add(stack);
			}
		} else
			is.add(get(args[0]));

		if (is.isEmpty() || is.get(0).getType() == Material.AIR)
			throw new RuntimeException("No item found!");

		return is;
	}

	public List<String> names(ItemStack stack) {
		ItemData itemData = new ItemData(stack);
		List<String> nameList = this.names.get(itemData);

		if (ListUtil.isEmpty(nameList)) {
			itemData = new ItemData(stack.getTypeId(), (short)0);
			nameList = this.names.get(itemData);
		}

		if (ListUtil.isEmpty(nameList))
			nameList = new ArrayList<>();

		if (nameList.size() > 15) nameList = nameList.subList(0, 15);
		return Collections.unmodifiableList(nameList);
	}

	public String name(ItemStack stack) {
		ItemData itemData = new ItemData(stack);
		String name = this.primaryName.get(itemData);

		if (StringUtil.isEmpty(name)) {
			itemData = new ItemData(stack.getTypeId(), (short)0);
			name = this.primaryName.get(itemData);
		}

		return StringUtil.isEmpty(name) ? "" : name;
	}

	public List<ItemData> parse(String[] itemList) throws NumberFormatException {
		if (ListUtil.isEmpty(itemList)) return Collections.emptyList();
		List<ItemData> itemDataList = new ArrayList<>();

		for (String item : itemList) {
			if (item.contains(":")) {
				String[] split = item.split(":");
				int itemNo = this.getId(split[0]);
				String itemData = split[1];

				if (NumberUtil.isInt(itemData))
					try { itemDataList.add(new ItemData(itemNo, Short.parseShort(itemData))); } catch (Exception ignore) { }
				else if (itemData.startsWith("[") && itemData.endsWith("]")) {
					String[] dataValueList = itemData.substring(1, itemData.length() - 1).split(",");

					for (String dataValue : dataValueList) {
						if (NumberUtil.isInt(dataValue))
							try { itemDataList.add(new ItemData(itemNo, Short.parseShort(dataValue))); } catch (Exception ignore) { }
						else if (dataValue.contains("-")) {
							String[] rangeData = dataValue.split("-");

							if (NumberUtil.isInt(rangeData[0]) && NumberUtil.isInt(rangeData[1])) {
								short start = Short.parseShort(rangeData[0]);
								short end = Short.parseShort(rangeData[1]);
								if (start > end) throw new IndexOutOfBoundsException(String.format("Data value start range %s must be below the end range %s!", start, end));
								for (short i = start; i < end; i++) try { itemDataList.add(new ItemData(itemNo, i)); } catch (Exception ignore) { }
							} else
								throw new NumberFormatException("Range data values must be an integer!");
						} else
							throw new NumberFormatException("List data value must be an integer or range!");
					}
				} else
					throw new NumberFormatException("Data value must be an integer, list or range!");
			} else
				try { itemDataList.add(new ItemData(this.getId(item))); } catch (Exception ignore) { }
		}

		return Collections.unmodifiableList(itemDataList);
	}

	public List<ItemData> parse(String itemColonList) throws NumberFormatException {
		if (StringUtil.isEmpty(itemColonList)) return Collections.emptyList();
		return this.parse(itemColonList.split(",(?![^\\[]*\\])"));
	}

	public List<String> getLines() {
		try {
			try (InputStreamReader inputStream = (this.itemsFile.exists() ? new FileReader(this.itemsFile) : new InputStreamReader(this.getPlugin().getResource(this.itemsFile.getName())))) {
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
		} catch (IOException ioex) {
			this.getLog().console(ioex);
			return Collections.emptyList();
		}
	}

	public void save() {
		this.save(false);
	}

	public void save(boolean replace) {
		this.getPlugin().saveResource(this.itemsFile.getName(), replace);
	}

	public void reload() {
		final List<String> lines = this.getLines();
		if (lines.isEmpty()) return;

		this.durabilities.clear();
		this.items.clear();
		this.names.clear();
		this.primaryName.clear();
		LengthCompare compare = new LengthCompare();

		for (String line : lines) {
			line = line.trim().toLowerCase(Locale.ENGLISH);
			if (!line.isEmpty() && line.charAt(0) == '#') continue;
			final String[] parts = line.split("[^a-zA-Z0-9]");
			if (parts.length < 2) continue;
			String itemName = parts[0].toLowerCase(Locale.ENGLISH);

			if (!NumberUtil.isInt(parts[1])) continue;
			final int numeric = Integer.parseInt(parts[1]);

			if (parts.length > 2)
				parts[2] = NumberUtil.isInt(parts[2]) ? parts[2] : "0";

			final short data = parts.length > 2 ? Short.parseShort(parts[2]) : 0;
			if (numeric < 0 || data < 0) continue;
			ItemData itemData = new ItemData(numeric, data);
			if (numeric > 0 && Material.AIR == Material.getMaterial(numeric)) continue;
			this.durabilities.put(itemName, data);
			this.items.put(itemName, numeric);

			if (!this.names.containsKey(itemData)) {
				this.names.put(itemData, new ArrayList<>(Collections.singletonList(itemName)));
				this.primaryName.put(itemData, itemName);
			} else {
				this.names.get(itemData).add(itemName);
				Collections.sort(this.names.get(itemData), compare);
			}
		}
	}

}