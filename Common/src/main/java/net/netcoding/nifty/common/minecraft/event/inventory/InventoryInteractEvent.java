package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.EventResult;

public interface InventoryInteractEvent extends InventoryEvent, Cancellable {

	EventResult getResult();

	HumanEntity getWhoClicked();

	void setResult(EventResult result);

}