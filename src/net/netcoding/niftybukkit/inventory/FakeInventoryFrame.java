package net.netcoding.niftybukkit.inventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.netcoding.niftybukkit.inventory.items.ItemData;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FakeInventoryFrame extends BukkitHelper {

	private final ConcurrentList<ItemStack> items = new ConcurrentList<>();
	private int totalSlots = -1;
	private boolean centered = false;
	private boolean autoCancel = false;
	private boolean shiftClickDisabled = false;

	FakeInventoryFrame(JavaPlugin plugin, boolean autoCancel) {
		super(plugin);
		this.setAutoCancelled(autoCancel);
	}

	public void add(ItemStack item) {
		this.items.add(item);
	}

	public void add(int index, ItemStack item) {
		this.items.add(index, item);
	}

	public void add(ItemData item, int amount, String displayName, String... lore) {
		this.add(this.items.size(), item, amount, displayName, Arrays.asList(lore));
	}

	public void add(ItemData item, int amount, String displayName, List<String> lore) {
		this.add(this.items.size(), item, amount, displayName, lore);
	}

	public void add(int index, ItemData item, int amount, String displayName, String... lore) {
		this.add(index, item, amount, displayName, Arrays.asList(lore));
	}

	@SuppressWarnings("deprecation")
	public void add(int index, ItemData item, int amount, String displayName, List<String> lore) {
		ItemStack stack = new ItemStack(item.getId(), amount, item.getData());
		ItemMeta meta = stack.getItemMeta();
		if (StringUtil.notEmpty(displayName)) meta.setDisplayName(displayName);
		if (ListUtil.notEmpty(lore)) meta.setLore(lore);
		stack.setItemMeta(meta);
		this.add(index, stack);
	}

	public void addAll(Collection<? extends ItemStack> collection) {
		this.items.addAll(collection);
	}

	public void addAll(int index, Collection<? extends ItemStack> collection) {
		this.items.addAll(index, collection);
	}

	public void centerItems() {
		this.centerItems(true);
	}

	public void centerItems(boolean center) {
		this.centered = center;
	}

	ConcurrentList<ItemStack> getItems() {
		return this.items;
	}

	ItemStack[] getItemsArray() {
		return ListUtil.toArray(this.items, ItemStack.class);
		//return this.items.toArray(new ItemStack[this.items.size()]);
	}

	public int getTotalSlots() {
		return this.totalSlots;
	}

	public boolean isAutoCancelled() {
		return this.autoCancel;
	}

	public boolean isItemsCentered() {
		return this.centered;
	}

	public boolean isShiftClickDisabled() {
		return this.shiftClickDisabled;
	}

	public void setAutoCancelled() {
		this.setAutoCancelled(true);
	}

	public void setAutoCancelled(boolean autoCancelled) {
		this.autoCancel = autoCancelled;
	}

	public void setShiftClickDisabled() {
		this.setshiftClickDisabled(true);
	}

	public void setshiftClickDisabled(boolean shiftClickDisabled) {
		this.shiftClickDisabled = shiftClickDisabled;
	}

	public void setTotalSlots(int totalSlots) {
		this.totalSlots = totalSlots >= 9 ? (totalSlots % 9 == 0 ? totalSlots : ((int)Math.ceil(totalSlots / 9) * 9)) : 9;
	}

}