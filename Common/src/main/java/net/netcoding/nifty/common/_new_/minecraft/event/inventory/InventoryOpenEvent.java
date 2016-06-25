package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;

public interface InventoryOpenEvent extends InventoryEvent, Cancellable {

	HumanEntity getPlayer();

}