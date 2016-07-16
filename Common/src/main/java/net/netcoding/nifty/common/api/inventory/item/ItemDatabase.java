package net.netcoding.nifty.common.api.inventory.item;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.util.misc.CSVStorage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ItemDatabase extends CSVStorage {

	private static final transient Pattern SPLIT_PATTERN = Pattern.compile("((.*)[:+',;.](\\d+))");
	private final ConcurrentLinkedMap<String, ItemData> items = Concurrent.newLinkedMap();

	protected ItemDatabase() {
		super(Nifty.getPlugin().getDataFolder(), "items");
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
		ConcurrentList<String> nameList = itemData.getNames();

		if (ListUtil.isEmpty(nameList))
			nameList = Concurrent.newList();

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

	@Override
	protected final void preReload() {
		this.items.clear();
	}

	public final Set<String> primaryNames() {
		return Collections.unmodifiableSet(this.items.keySet());
	}

	@Override
	public void processLine(String[] parts) {
		if (parts.length < 2) return;
		String itemName = parts[0].toLowerCase(Locale.ENGLISH);
		if (!NumberUtil.isNumber(parts[1])) return;
		final int numeric = Integer.parseInt(parts[1]);
		if (parts.length > 2) parts[2] = NumberUtil.isNumber(parts[2]) ? parts[2] : "0";
		final short data = parts.length > 2 ? Short.parseShort(parts[2]) : 0;
		if (numeric < 0 || data < 0) return;

		try {
			Material material = Material.getMaterial(numeric);
			if (numeric > 0 && Material.AIR == material) return;
			ItemStack itemStack = ItemStack.builder().type(material, false).type(data).build();
			String primaryName = itemStack.getType().name();
			ItemData itemData;

			if (!this.items.containsKey(primaryName))
				this.items.put(primaryName, itemData = new ItemData(itemStack.getTypeId(), primaryName));
			else
				(itemData = this.items.get(primaryName)).addName(itemName);

			itemData.addDurability(data);
		} catch (Exception ignore) { }
	}

}