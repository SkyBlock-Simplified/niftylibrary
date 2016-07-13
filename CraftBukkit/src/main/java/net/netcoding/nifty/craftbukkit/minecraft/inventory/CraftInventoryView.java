package net.netcoding.nifty.craftbukkit.minecraft.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftEntity;

public final class CraftInventoryView implements InventoryView {

	private final org.bukkit.inventory.InventoryView inventoryView;
	private final Inventory topInventory;
	private final Inventory bottomInventory;
	private final HumanEntity viewer;

	public CraftInventoryView(org.bukkit.inventory.InventoryView inventoryView) {
		this.inventoryView = inventoryView;
		this.topInventory = CraftInventory.convertBukkitInventory(inventoryView.getTopInventory());
		this.bottomInventory = CraftInventory.convertBukkitInventory(inventoryView.getBottomInventory());
		this.viewer = CraftEntity.convertBukkitEntity(inventoryView.getPlayer(), HumanEntity.class);
	}

	public org.bukkit.inventory.InventoryView getHandle() {
		return this.inventoryView;
	}

	@Override
	public Inventory getTopInventory() {
		return this.topInventory;
	}

	@Override
	public Inventory getBottomInventory() {
		return this.bottomInventory;
	}

	@Override
	public HumanEntity getViewer() {
		return this.viewer;
	}

	@Override
	public InventoryType getType() {
		return InventoryType.valueOf(this.getHandle().getType().name());
	}

}