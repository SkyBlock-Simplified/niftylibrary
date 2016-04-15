package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.inventory.items.ItemData;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public abstract class FakeInventoryFrame extends BukkitListener implements Iterable<ItemData> {

	private ConcurrentList<ItemData> items = new ConcurrentList<>();
	private ConcurrentMap<String, Object> metadata = new ConcurrentMap<>();
	private int totalSlots = -1;
	private boolean centered = false;
	private boolean autoCancel = false;
	private boolean tradingEnabled = false;
	private String title = "";

	FakeInventoryFrame(JavaPlugin plugin) {
		super(plugin);
	}

	FakeInventoryFrame(FakeInventoryFrame frame) {
		super(frame.getPlugin());
		this.items = frame.items;
		this.metadata = frame.metadata;
		this.totalSlots = frame.totalSlots;
		this.centered = frame.centered;
		this.autoCancel = frame.autoCancel;
		this.tradingEnabled = frame.tradingEnabled;
		this.title = frame.title;
	}

	public void add(int index, ItemData item) {
		this.items.add(index, item);
	}

	public void add(int index, ItemStack item) {
		this.add(index, new ItemData(item));
	}

	public void add(ItemData item) {
		this.items.add(item);
	}

	public void add(ItemStack item) {
		this.add(new ItemData(item));
	}

	public void addAll(Collection<? extends ItemData> collection) {
		this.items.addAll(collection);
	}

	public void addAll(int index, Collection<? extends ItemData> collection) {
		this.items.addAll(index, collection);
	}

	protected int calculateTotalSlots(int value) {
		return value >= 9 ? (value % 9 == 0 ? value : ((int)Math.ceil(value / 9.0) * 9)) : 9;
	}

	public void clearMetadata() {
		this.metadata.clear();
	}

	ConcurrentList<ItemData> getItems() {
		return this.items;
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotalSlots() {
		return this.totalSlots >= this.getItems().size() ? this.totalSlots : this.calculateTotalSlots(this.getItems().size());
	}

	public boolean hasMetadata(String key) {
		return this.metadata.containsKey(key);
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

	@Override
	public Iterator<ItemData> iterator() {
		return Collections.unmodifiableList(this.items).iterator();
	}

	public void removeMetadata(String key) {
		this.metadata.remove(key);
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

	public void setMetadata(String key, Object obj) {
		this.metadata.put(key, obj);
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