package net.netcoding.niftybukkit.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface FakeInventoryListener {

	public void onInventoryClose(InventoryCloseEvent event);

	public void onInventoryClick(InventoryClickEvent event);

	public void onInventoryOpen(InventoryOpenEvent event);

}