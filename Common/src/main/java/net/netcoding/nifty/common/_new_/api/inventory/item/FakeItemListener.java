package net.netcoding.nifty.common._new_.api.inventory.item;

import net.netcoding.nifty.common._new_.api.inventory.events.FakeItemInteractEvent;

public interface FakeItemListener {

	void onItemInteract(FakeItemInteractEvent event);

}