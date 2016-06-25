package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.entity.living.HumanEntity;

public interface InventoryCloseEvent extends InventoryEvent {

	HumanEntity getPlayer();

}