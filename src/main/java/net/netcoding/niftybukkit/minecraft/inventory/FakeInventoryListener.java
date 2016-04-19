package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.minecraft.inventory.events.InventoryOpenEvent;

public interface FakeInventoryListener {

	void onInventoryClick(InventoryClickEvent event);

	void onInventoryClose(InventoryCloseEvent event);

	void onInventoryItemInteract(InventoryItemInteractEvent event);

	void onInventoryOpen(InventoryOpenEvent event);

}