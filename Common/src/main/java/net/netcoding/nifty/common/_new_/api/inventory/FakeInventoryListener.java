package net.netcoding.nifty.common._new_.api.inventory;

import net.netcoding.nifty.common._new_.api.inventory.events.FakeInventoryClickEvent;
import net.netcoding.nifty.common._new_.api.inventory.events.FakeInventoryCloseEvent;
import net.netcoding.nifty.common._new_.api.inventory.events.FakeInventoryOpenEvent;
import net.netcoding.nifty.common._new_.api.inventory.item.FakeItemListener;

public interface FakeInventoryListener extends FakeItemListener {

	void onInventoryClick(FakeInventoryClickEvent event);

	void onInventoryClose(FakeInventoryCloseEvent event);

	void onInventoryOpen(FakeInventoryOpenEvent event);

}