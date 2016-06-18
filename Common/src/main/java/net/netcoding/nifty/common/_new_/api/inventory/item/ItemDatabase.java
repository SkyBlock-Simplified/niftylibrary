package net.netcoding.nifty.common._new_.api.inventory.item;

import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.minecraft.material.Material;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.niftycore.util.misc.CSVStorage;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.NumberUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.comparator.LengthCompare;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;
import net.netcoding.niftycore.util.concurrent.linked.ConcurrentLinkedMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ItemDatabase extends CSVStorage {

	public final class ItemData {

		private final ConcurrentSet<Short> durabilities = new ConcurrentSet<>();
		private final ConcurrentList<String> names = new ConcurrentList<>();
		private final String primaryName;
		private final int id;

		public ItemData(int id, String primaryName) {
			this.id = id;
			this.primaryName = primaryName;
			this.names.add(primaryName);
		}

		public void addDurability(short durability) {
			this.durabilities.add(durability);
		}

		public void addName(String name) {
			this.names.add(name);
			Collections.sort(this.names, LENGTH_COMPARE);
		}

		public Set<Short> getDurabilities() {
			return this.durabilities;
		}

		public final int getId() {
			return this.id;
		}

		public List<String> getNames() {
			return this.names;
		}

		public String getPrimaryName() {
			return this.primaryName;
		}

	}

	private static final LengthCompare LENGTH_COMPARE = new LengthCompare();
	private final static transient Pattern SPLIT_PATTERN = Pattern.compile("((.*)[:+',;.](\\d+))");
	private final ConcurrentLinkedMap<String, ItemData> items = new ConcurrentLinkedMap<>();

	protected ItemDatabase() {
		super(Nifty.getPlugin().getPluginDescription().getStorage(), "items");
	}

	public final ItemStack get(final String id) throws RuntimeException {
		if (StringUtil.isEmpty(id)) throw new NullPointerException("The value for id cannot be null!");
		int itemid = -1;
		String itemName = id;
		short metaData = 0;
		Matcher parts = SPLIT_PATTERN.matcher(id);

		if (parts.matches()) {
			itemName = parts.group(2);
			metaData = Short.parseShort(parts.group(3));
		}

		if (NumberUtil.isNumber(itemName))
			itemid = Integer.parseInt(itemName);
		else if (NumberUtil.isNumber(id))
			itemid = Integer.parseInt(id);
		else
			itemName = itemName.toLowerCase(Locale.ENGLISH);

		if (itemid < 0) {
			if (this.items.containsKey(itemName)) {
				ItemData itemData = this.items.get(itemName);
				itemid = itemData.getId();

				if (itemData.getDurabilities().contains(itemName) && metaData == 0)
					metaData = itemData.getDurabilities().iterator().next();
			} else if (Material.getMaterial(itemName.toUpperCase(Locale.ENGLISH)) != null)
				itemid = Material.getMaterial(itemName.toUpperCase(Locale.ENGLISH)).getId();
			else
				throw new RuntimeException("Unknown item name: " + itemName);
		}

		if (itemid < 0) throw new RuntimeException("Unknown item name: " + itemName);
		Material mat = Material.getMaterial(itemid);
		if (mat == null) throw new RuntimeException("Unknown item id: " + itemid);
		ItemStack retval = ItemStack.of(mat);
		retval.setAmount(mat.getMaxStackSize());
		retval.setDurability(metaData);
		return retval;
	}

	public final ItemStack get(final String id, final int quantity) throws RuntimeException {
		final ItemStack retval = get(id.toLowerCase(Locale.ENGLISH));
		retval.setAmount(quantity);
		return retval;
	}

	public final int getId(final String itemName) throws RuntimeException {
		return this.get(itemName).getTypeId();
	}

	public final List<ItemStack> getMatching(Player player, String[] args) throws RuntimeException {
		List<ItemStack> is = new ArrayList<>();

		if (args.length < 1)
			is.add(ItemStack.of(player.getItemInHand()));
		else if ("hand".equalsIgnoreCase(args[0]))
			is.add(ItemStack.of(player.getItemInHand()));
		else if ("inventory".equalsIgnoreCase(args[0]) || "invent".equalsIgnoreCase(args[0]) || "all".equalsIgnoreCase(args[0])) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR) continue;
				is.add(ItemStack.of(stack));
			}
		} else if ("blocks".equalsIgnoreCase(args[0])) {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR || !stack.getType().isBlock()) continue;
				is.add(ItemStack.of(stack));
			}
		} else
			is.add(get(args[0]));

		if (is.isEmpty() || is.get(0).getType() == Material.AIR)
			throw new RuntimeException("No item found!");

		return is;
	}

	public final String name(ItemStack stack) {
		ItemData itemData = this.items.get(stack.getType().name());
		String name = itemData.getPrimaryName();
		return StringUtil.isEmpty(name) ? "" : name;
	}

	public final List<String> names(ItemStack stack) {
		ItemData itemData = this.items.get(stack.getType().name());
		List<String> nameList = itemData.getNames();

		if (ListUtil.isEmpty(nameList))
			nameList = new ConcurrentList<>();

		if (nameList.size() > 15)
			nameList = nameList.subList(0, 15);

		return Collections.unmodifiableList(nameList);
	}

	public final List<ItemStack> parse(String[] itemList) throws NumberFormatException {
		if (ListUtil.isEmpty(itemList)) return Collections.emptyList();
		List<ItemStack> itemDataList = new ArrayList<>();

		for (String item : itemList) {
			if (item.contains(":")) {
				String[] split = item.split(":");
				int itemNo = this.getId(split[0]);
				String itemData = split[1];

				if (NumberUtil.isNumber(itemData))
					try { itemDataList.add(ItemStack.of(itemNo, Short.parseShort(itemData))); } catch (Exception ignore) { }
				else if (itemData.startsWith("[") && itemData.endsWith("]")) {
					String[] dataValueList = itemData.substring(1, itemData.length() - 1).split(",");

					for (String dataValue : dataValueList) {
						if (NumberUtil.isNumber(dataValue))
							try { itemDataList.add(ItemStack.of(itemNo, Short.parseShort(dataValue))); } catch (Exception ignore) { }
						else if (dataValue.contains("-")) {
							String[] rangeData = dataValue.split("-");

							if (NumberUtil.isNumber(rangeData[0]) && NumberUtil.isNumber(rangeData[1])) {
								short start = Short.parseShort(rangeData[0]);
								short end = Short.parseShort(rangeData[1]);
								if (start > end) throw new IndexOutOfBoundsException(String.format("Data value start range %s must be below the end range %s!", start, end));
								for (short i = start; i < end; i++) try { itemDataList.add(ItemStack.of(itemNo, i)); } catch (Exception ignore) { }
							} else
								throw new NumberFormatException("Range data values must be an integer!");
						} else
							throw new NumberFormatException("List data value must be an integer or range!");
					}
				} else
					throw new NumberFormatException("Data value must be an integer, list or range!");
			} else
				try { itemDataList.add(ItemStack.of(this.getId(item))); } catch (Exception ignore) { }
		}

		return Collections.unmodifiableList(itemDataList);
	}

	public final List<ItemStack> parse(String itemColonList) throws NumberFormatException {
		if (StringUtil.isEmpty(itemColonList)) return Collections.emptyList();
		return this.parse(itemColonList.split(",(?![^\\[]*\\])"));
	}

	public final Set<String> primaryNames() {
		return Collections.unmodifiableSet(this.items.keySet());
	}

	@Override
	public void reload() {
		try {
			final List<String> lines = this.getLines();
			if (lines.isEmpty()) return;

			this.items.clear();

			for (String line : lines) {
				line = line.trim().toLowerCase(Locale.ENGLISH);
				if (!line.isEmpty() && line.charAt(0) == '#') continue;
				final String[] parts = line.split("[^a-zA-Z0-9]");
				if (parts.length < 2) continue;
				String itemName = parts[0].toLowerCase(Locale.ENGLISH);
				if (!NumberUtil.isNumber(parts[1])) continue;
				final int numeric = Integer.parseInt(parts[1]);
				if (parts.length > 2) parts[2] = NumberUtil.isNumber(parts[2]) ? parts[2] : "0";
				final short data = parts.length > 2 ? Short.parseShort(parts[2]) : 0;
				if (numeric < 0 || data < 0) continue;

				try {
					Material material = Material.getMaterial(numeric);
					if (numeric > 0 && Material.AIR == material) continue;
					ItemStack itemStack = ItemStack.of(material, data); // TODO: Don't initialize NBT
					String primaryName = itemStack.getType().name();
					ItemData itemData;

					if (!this.items.containsKey(primaryName))
						this.items.put(primaryName, itemData = new ItemData(itemStack.getTypeId(), primaryName));
					else
						(itemData = this.items.get(primaryName)).addName(itemName);

					itemData.addDurability(data);
				} catch (Exception ignore) {
				}
			}
		} catch (IOException ioex) {
			Nifty.getLog().console("Unable to read ''{0}''!", ioex, this.getLocalFile().getName());
		}
	}

}