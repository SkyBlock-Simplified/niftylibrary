package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.EventResult;

public interface InventoryInteractEvent extends InventoryEvent, Cancellable {

	EventResult getResult();

	HumanEntity getWhoClicked();

	void setResult(EventResult result);

}