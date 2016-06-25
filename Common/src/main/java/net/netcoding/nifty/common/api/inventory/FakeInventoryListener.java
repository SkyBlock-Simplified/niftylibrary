package net.netcoding.nifty.common.api.inventory;

import net.netcoding.nifty.common.api.inventory.events.FakeInventoryClickEvent;
import net.netcoding.nifty.common.api.inventory.events.FakeInventoryCloseEvent;
import net.netcoding.nifty.common.api.inventory.events.FakeInventoryOpenEvent;
import net.netcoding.nifty.common.api.inventory.item.FakeItemListener;

public interface FakeInventoryListener extends FakeItemListener {

	void onInventoryClick(FakeInventoryClickEvent event);

	void onInventoryClose(FakeInventoryCloseEvent event);

	void onInventoryOpen(FakeInventoryOpenEvent event);

}