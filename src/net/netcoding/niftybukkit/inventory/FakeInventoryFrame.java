package net.netcoding.niftybukkit.inventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.netcoding.niftybukkit.items.ItemData;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.utilities.ConcurrentList;

import org.bukkit.Material;
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

	public void add(int index, ItemStack item) {
		this.items.add(index, item);
	}

	@SuppressWarnings("deprecation")
	public boolean add(ItemData item, int amount, String displayName, List<String> lore) {
		ItemStack stack = new ItemStack(item.getId(), amount, item.getData());
		ItemMeta meta = stack.getItemMeta();
		if (!"".equals(displayName)) meta.setDisplayName(displayName);
		List<String> combinedLore = meta.getLore();
		if (lore != null && lore.size() > 0) combinedLore.addAll(lore);
		meta.setLore(combinedLore);
		stack.setItemMeta(meta);
		return this.add(stack);
	}

	public boolean add(ItemData item, int amount, String displayName, String... lore) {
		return this.add(item, amount, displayName, Arrays.asList(lore));
	}

	public boolean add(ItemStack item) {
		return this.items.add(item);
	}

	public boolean add(Material material) {
		return this.add(material, (short)0);
	}

	public boolean add(Material material, short data) {
		return this.add(material, data, 1);
	}

	public boolean add(Material material, short data, int amount) {
		return this.add(material, data, amount, null);
	}

	@SuppressWarnings("deprecation")
	public boolean add(Material material, short data, int amount, String displayName, List<String> lore) {
		return this.add(new ItemData(material.getId(), data), amount, displayName, lore);
	}

	public boolean add(Material material, short data, int amount, String displayName, String... lore) {
		return this.add(material, data, amount, displayName, Arrays.asList(lore));
	}

	public boolean addAll(Collection<? extends ItemStack> collection) {
		return this.items.addAll(collection);
	}

	public boolean addAll(int index, Collection<? extends ItemStack> collection) {
		return this.items.addAll(index, collection);
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
		this.totalSlots = totalSlots >= 9 ? (totalSlots % 9 == 0 ? totalSlots : (Math.round(totalSlots / 9) * 9)) : 9;
	}

}