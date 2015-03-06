package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.inventory.events.InventoryClickEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryCloseEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryItemInteractEvent;
import net.netcoding.niftybukkit.inventory.events.InventoryOpenEvent;

public interface FakeInventoryListener {

	public void onInventoryClick(InventoryClickEvent event);

	public void onInventoryClose(InventoryCloseEvent event);

	public void onInventoryOpen(InventoryOpenEvent event);

	public void onInventoryItemInteract(InventoryItemInteractEvent event);

}