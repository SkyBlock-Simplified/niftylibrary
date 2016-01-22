package net.netcoding.niftybukkit.inventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.inventory.items.ItemData;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FakeInventoryFrame extends BukkitListener {

	private ConcurrentList<ItemStack> items = new ConcurrentList<>();
	private int totalSlots = -1;
	private boolean centered = false;
	private boolean autoCancel = false;
	private boolean tradingEnabled = false;
	private String title = "";

	FakeInventoryFrame(JavaPlugin plugin) {
		super(plugin);
	}

	public void add(int index, ItemData item, int amount, String displayName, List<String> lore) {
		ItemStack stack = NiftyBukkit.getItemDatabase().get(StringUtil.format("{0}:{1}", item.getTypeId(), item.getData()), amount);
		ItemMeta meta = stack.getItemMeta();
		if (StringUtil.notEmpty(displayName)) meta.setDisplayName(displayName);
		if (ListUtil.notEmpty(lore)) meta.setLore(lore);
		stack.setItemMeta(meta);
		this.add(index, stack);
	}

	public void add(int index, ItemData item, int amount, String displayName, String... lore) {
		this.add(index, item, amount, displayName, Arrays.asList(lore));
	}

	public void add(int index, ItemStack item) {
		this.items.add(index, item);
	}

	public void add(ItemData item, int amount, String displayName, List<String> lore) {
		this.add(this.items.size(), item, amount, displayName, lore);
	}

	public void add(ItemData item, int amount, String displayName, String... lore) {
		this.add(this.items.size(), item, amount, displayName, Arrays.asList(lore));
	}

	public void add(ItemStack item) {
		this.items.add(item);
	}

	public void addAll(Collection<? extends ItemStack> collection) {
		this.items.addAll(collection);
	}

	public void addAll(int index, Collection<? extends ItemStack> collection) {
		this.items.addAll(index, collection);
	}

	protected int calculateTotalSlots(int value) {
		return value >= 9 ? (value % 9 == 0 ? value : ((int)Math.ceil(value / 9.0) * 9)) : 9;
	}

	ConcurrentList<ItemStack> getItems() {
		return this.items;
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotalSlots() {
		return this.totalSlots >= this.getItems().size() ? this.totalSlots : this.calculateTotalSlots(this.getItems().size());
	}

	public boolean isAutoCancelled() {
		return this.autoCancel;
	}

	public boolean isAutoCentered() {
		return this.centered;
	}

	public boolean isTradingEnabled() {
		return this.tradingEnabled;
	}

	public void setAutoCancelled() {
		this.setAutoCancelled(true);
	}

	public void setAutoCancelled(boolean value) {
		this.autoCancel = value;
	}

	public void setAutoCenter() {
		this.setAutoCenter(true);
	}

	public void setAutoCenter(boolean value) {
		this.centered = value;
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public void setTotalSlots(int value) {
		this.totalSlots = this.calculateTotalSlots(value);
	}

	public void setTradingEnabled() {
		this.setTradingEnabled(true);
	}

	public void setTradingEnabled(boolean value) {
		this.tradingEnabled = value;
	}

}