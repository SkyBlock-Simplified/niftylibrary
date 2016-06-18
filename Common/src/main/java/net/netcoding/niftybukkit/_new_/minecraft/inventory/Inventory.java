package net.netcoding.niftybukkit._new_.minecraft.inventory;

import net.netcoding.niftybukkit._new_.minecraft.entity.living.HumanEntity;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.material.Material;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public interface Inventory extends Iterable<ItemStack> {

	String getName();

	int getMaxStackSize();

	int getSize();

	void setMaxStackSize(int var1);

	ItemStack getItem(int var1);

	void setItem(int var1, ItemStack var2);

	HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) throws IllegalArgumentException;

	HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) throws IllegalArgumentException;

	ItemStack[] getContents();

	void setContents(ItemStack[] var1) throws IllegalArgumentException;

	ItemStack[] getStorageContents();

	void setStorageContents(ItemStack[] var1) throws IllegalArgumentException;

	/** @deprecated */
	@Deprecated
	boolean contains(int var1);

	boolean contains(Material var1) throws IllegalArgumentException;

	boolean contains(ItemStack var1);

	/** @deprecated */
	@Deprecated
	boolean contains(int var1, int var2);

	boolean contains(Material var1, int var2) throws IllegalArgumentException;

	boolean contains(ItemStack var1, int var2);

	boolean containsAtLeast(ItemStack var1, int var2);

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

	ListIterator<ItemStack> iterator();

	ListIterator<ItemStack> iterator(int var1);

	Location getLocation();

}