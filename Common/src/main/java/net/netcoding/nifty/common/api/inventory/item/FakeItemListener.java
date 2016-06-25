package net.netcoding.nifty.common.api.inventory.item;

import net.netcoding.nifty.common.api.inventory.events.FakeItemInteractEvent;

public interface FakeItemListener {

	void onItemInteract(FakeItemInteractEvent event);

}