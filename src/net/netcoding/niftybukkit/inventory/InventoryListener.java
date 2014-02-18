package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.inventory.events.*;

public interface InventoryListener {

	public void onInventoryClose(InventoryCloseEvent event);

	public void onInventoryInteract(InventoryInteractEvent event);

	public void onInventoryOpen(InventoryOpenEvent event);

}