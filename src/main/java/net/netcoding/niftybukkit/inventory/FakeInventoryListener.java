package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryOpenEvent;

public interface FakeInventoryListener {

	void onInventoryClick(InventoryClickEvent event);

	void onInventoryClose(InventoryCloseEvent event);

	void onInventoryOpen(InventoryOpenEvent event);

	void onInventoryItemInteract(InventoryItemInteractEvent event);

}