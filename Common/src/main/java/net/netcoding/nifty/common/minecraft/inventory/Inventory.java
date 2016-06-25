package net.netcoding.nifty.common.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.region.Location;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public interface Inventory extends Iterable<ItemStack> {

	String getName();

	int getMaxStackSize();

	int getSize();

	void setMaxStackSize(int slot);

	ItemStack getItem(int slot);

	void setItem(int slot, ItemStack item);

	HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException;

	HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException;

	ItemStack[] getContents();

	void setContents(ItemStack[] items) throws IllegalArgumentException;

	ItemStack[] getStorageContents();

	void setStorageContents(ItemStack[] items) throws IllegalArgumentException;

	/** @deprecated */
	@Deprecated
	boolean contains(int var1);

	boolean contains(Material material) throws IllegalArgumentException;

	default boolean contains(ItemStack item) {
		return this.contains(item.getType());
	}

	/** @deprecated */
	@Deprecated
	boolean contains(int var1, int var2);

	boolean contains(Material material, int slot) throws IllegalArgumentException;

	default boolean contains(ItemStack item, int slot) {
		return this.contains(item.getType(), slot);
	}

	boolean containsAtLeast(ItemStack item, int slot);

	/** @deprecated */
	@Deprecated
	HashMap<Integer, ? extends ItemStack> all(int var1);

	HashMap<Integer, ? extends ItemStack> all(Material var1) throws IllegalArgumentException;

	HashMap<Integer, ? extends ItemStack> all(ItemStack var1);

	/** @deprecated */
	@Deprecated
	int first(int var1);

	int first(Material var1) throws IllegalArgumentException;

	int first(ItemStack var1);

	int firstEmpty();

	/** @deprecated */
	@Deprecated
	void remove(int var1);

	void remove(Material var1) throws IllegalArgumentException;

	void remove(ItemStack var1);

	void clear(int var1);

	void clear();

	List<HumanEntity> getViewers();

	String getTitle();

	InventoryType getType();

	InventoryHolder getHolder();

	@Override
	ListIterator<ItemStack> iterator();

	ListIterator<ItemStack> iterator(int var1);

	Location getLocation();

}