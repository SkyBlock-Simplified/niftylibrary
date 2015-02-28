package net.netcoding.niftybukkit.inventory.items;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.NumberUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemDatabase extends BukkitHelper {

	private final transient File itemsFile;
	private final transient Map<String, Integer> items = new HashMap<String, Integer>();
	private final transient Map<ItemData, List<String>> names = new HashMap<ItemData, List<String>>();
	private final transient Map<ItemData, String> primaryName = new HashMap<ItemData, String>();
	private final transient Map<String, Short> durabilities = new HashMap<String, Short>();
	private final static transient Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

	public ItemDatabase(JavaPlugin plugin) {
		this(plugin, "items.csv");
	}

	public ItemDatabase(JavaPlugin plugin, String fileName) {
		super(plugin);
		this.itemsFile = new File(this.getPlugin().getDataFolder(), fileName);
	}

	@SuppressWarnings("deprecation")
	public ItemStack get(final String id) throws RuntimeException {
		int itemid = 0;
		String itemname = id;
		short metaData = 0;
		Matcher parts = splitPattern.matcher(id);

		if (parts.matches()) {
			itemname = parts.group(2);
			metaData = Short.parseShort(parts.group(3));
		}

		if (NumberUtil.isInt(itemname))
			itemid = Integer.parseInt(itemname);
		else if (NumberUtil.isInt(id))
			itemid = Integer.parseInt(id);
		else
			itemname = itemname.toLowerCase(Locale.ENGLISH);

		if (itemid < 0) {
			if (this.items.containsKey(itemname)) {
				itemid = this.items.get(itemname);

				if (this.durabilities.containsKey(itemname) && metaData == 0)
					metaData = this.durabilities.get(itemname);
			} else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null)
				itemid = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)).getId();
			else
				throw new RuntimeException("Unknown item name: " + itemname);
		}

		if (itemid < 0) throw new RuntimeException("Unknown item name: " + itemname);
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

	@SuppressWarnings("deprecation")
	public int getId(final String itemName) throws RuntimeException {
		return this.get(itemName).getTypeId();
	}

	@SuppressWarnings("deprecation")
	public List<ItemStack> getMatching(Player player, String[] args) throws RuntimeException {
		List<ItemStack> is = new ArrayList<ItemStack>();

		if (args.length < 1)
			is.add(player.getItemInHand());
		else if (args[0].equalsIgnoreCase("hand"))
			is.add(player.getItemInHand());
		else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("invent") || args[0].equalsIgnoreCase("all")) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR) continue;
				is.add(stack);
			}
		} else if (args[0].equalsIgnoreCase("blocks")) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getTypeId() > 255 || stack.getType() == Material.AIR) continue;
				is.add(stack);
			}
		} else
			is.add(get(args[0]));

		if (is.isEmpty()/* || is.get(0).getType().equals(Material.AIR)*/)
			throw new RuntimeException("No item found!");

		return is;
	}

	@SuppressWarnings("deprecation")
	public List<String> names(ItemStack item) {
		ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
		List<String> nameList = this.names.get(itemData);

		if (nameList == null) {
			itemData = new ItemData(item.getTypeId(), (short)0);
			nameList = this.names.get(itemData);
			if (nameList == null) return Collections.emptyList();
		}

		if (nameList.size() > 15) nameList = nameList.subList(0, 14);
		return nameList;
	}

	@SuppressWarnings("deprecation")
	public String name(ItemStack item) {
		ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
		String name = this.primaryName.get(itemData);

		if (name == null) {
			itemData = new ItemData(item.getTypeId(), (short)0);
			name = this.primaryName.get(itemData);
			if (name == null) return null;
		}

		return name;
	}

	public List<ItemData> parse(String itemCommaList) throws NumberFormatException {
		List<ItemData> itemDataList = new ArrayList<>();
		if (StringUtil.isEmpty(itemCommaList)) return Collections.unmodifiableList(itemDataList);
		String[] itemList = StringUtil.stripNull(itemCommaList).split(",(?![^\\[]*\\])");
		if (ListUtil.isEmpty(itemList)) return Collections.unmodifiableList(itemDataList);

		for (String item : itemList) {
			if (item.contains(":")) {
				String[] split = item.split(":");
				int itemNo = this.getId(split[0]);
				String itemData = split[1];

				if (NumberUtil.isInt(itemData))
					itemDataList.add(new ItemData(itemNo, Short.parseShort(itemData)));
				else if (itemData.startsWith("[") && itemData.endsWith("]")) {
					String[] dataValueList = itemData.substring(1, itemData.length() - 1).split(",");

					for (String dataValue : dataValueList) {
						if (NumberUtil.isInt(dataValue))
							itemDataList.add(new ItemData(itemNo, Short.parseShort(dataValue)));
						else if (dataValue.contains("-")) {
							String[] rangeData = dataValue.split("-");

							if (NumberUtil.isInt(rangeData[0]) && NumberUtil.isInt(rangeData[1])) {
								short start = Short.parseShort(rangeData[0]);
								short end = Short.parseShort(rangeData[1]);
								if (start > end) throw new IndexOutOfBoundsException(String.format("Data value start range %s must be below the end range %s!", start, end));
								for (short i = start; i < end; i++) itemDataList.add(new ItemData(itemNo, i));
							} else
								throw new NumberFormatException("Range data values must be an integer!");
						} else
							throw new NumberFormatException("List data value must be an integer or range!");
					}
				} else
					throw new NumberFormatException("Data value must be an integer, list or range!");

			} else
				itemDataList.add(new ItemData(this.getId(item)));
		}

		return Collections.unmodifiableList(itemDataList);
	}

	public List<String> getLines() {
		try {
			final BufferedReader reader;

			if (this.itemsFile.exists())
				reader = new BufferedReader(new FileReader(this.itemsFile));
			else
				reader = new BufferedReader(new InputStreamReader(this.getPlugin().getResource(this.itemsFile.getName())));

			try {
				final List<String> lines = new ArrayList<String>();

				do {
					final String line = reader.readLine();
					if (line == null) break; else lines.add(line);
				} while (true);

				return Collections.unmodifiableList(lines);
			} finally {
				reader.close();
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
			if (line.length() > 0 && line.charAt(0) == '#') continue;
			final String[] parts = line.split("[^a-z0-9]");
			if (parts.length < 2) continue;
			final int numeric = Integer.parseInt(parts[1]);
			final short data = parts.length > 2 && !parts[2].equals("0") ? Short.parseShort(parts[2]) : 0;
			String itemName = parts[0].toLowerCase(Locale.ENGLISH);
			this.durabilities.put(itemName, data);
			this.items.put(itemName, numeric);
			ItemData itemData = new ItemData(numeric, data);

			if (!this.names.containsKey(itemData)) {
				this.names.put(itemData, new ArrayList<>(Arrays.asList(itemName)));
				this.primaryName.put(itemData, itemName);
			} else {
				this.names.get(itemData).add(itemName);
				Collections.sort(this.names.get(itemData), compare);
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