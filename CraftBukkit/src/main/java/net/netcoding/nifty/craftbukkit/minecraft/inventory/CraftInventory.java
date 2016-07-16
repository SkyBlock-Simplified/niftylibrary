package net.netcoding.nifty.craftbukkit.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.minecraft.block.state.CraftBlockState;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CraftInventory implements Inventory {

	private final org.bukkit.inventory.Inventory inventory;
	private final Location location;

	public CraftInventory(org.bukkit.inventory.Inventory inventory) {
		this.inventory = inventory;
		this.location = (inventory.getLocation() != null ? new CraftLocation(inventory.getLocation()) : null);
	}

	@Override
	public Map<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		org.bukkit.inventory.ItemStack[] bukkitItems = Arrays.stream(items).map(CraftConverter::toBukkitItem).toArray(org.bukkit.inventory.ItemStack[]::new);
		return this.getHandle().addItem(bukkitItems).entrySet().stream().collect(Concurrent.toMap(Map.Entry::getKey, entry -> new CraftItemStack(entry.getValue())));
	}

	@Override
	public void clear(int index) {
		this.getHandle().clear(index);
	}

	public static Inventory convertBukkitInventory(org.bukkit.inventory.Inventory bukkitInventory) {
		return convertBukkitInventory(bukkitInventory, Inventory.class);
	}

	public static <T extends Inventory> T convertBukkitInventory(org.bukkit.inventory.Inventory bukkitInventory, Class<T> inventoryType) {
		CraftInventoryType type = CraftInventoryType.getByBukkitClass(bukkitInventory.getClass());
		return inventoryType.cast(new Reflection(type.getInventoryClass()).newInstance(bukkitInventory));
	}

	@Override
	public ItemStack[] getContents() {
		return Arrays.stream(this.getHandle().getContents()).map(CraftItemStack::new).toArray(CraftItemStack[]::new);
	}

	public org.bukkit.inventory.Inventory getHandle() {
		return this.inventory;
	}

	@Override
	public InventoryHolder getHolder() {
		org.bukkit.inventory.InventoryHolder bukkitHolder = this.getHandle().getHolder();
		return (bukkitHolder instanceof org.bukkit.block.BlockState) ?
				(InventoryHolder)CraftBlockState.convertBukkitState((org.bukkit.block.BlockState)bukkitHolder) :
				(InventoryHolder)CraftEntity.convertBukkitEntity((org.bukkit.entity.Entity)bukkitHolder);
	}

	@Override
	public ItemStack getItem(int index) {
		return new CraftItemStack(this.getHandle().getItem(index));
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public int getMaxStackSize() {
		return this.getHandle().getMaxStackSize();
	}

	@Override
	public int getSize() {
		return this.getHandle().getSize();
	}

	@Override
	public String getTitle() {
		return this.getHandle().getTitle();
	}

	@Override
	public InventoryType getType() {
		return InventoryType.valueOf(this.getHandle().getType().name());
	}

	@Override
	public List<HumanEntity> getViewers() {
		return this.getHandle().getViewers().stream().map(entity -> CraftEntity.convertBukkitEntity(entity, HumanEntity.class)).collect(Concurrent.toList());
	}

	@Override
	public void setItem(int index, ItemStack item) {
		this.getHandle().setItem(index, CraftConverter.toBukkitItem(item));
	}

	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		this.getHandle().setContents(Arrays.stream(items).map(CraftConverter::toBukkitItem).toArray(org.bukkit.inventory.ItemStack[]::new));
	}

	@Override
	public void setMaxStackSize(int size) {
		this.getHandle().setMaxStackSize(size);
	}

}