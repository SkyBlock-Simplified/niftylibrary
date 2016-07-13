package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.human.HumanEntity;

public interface InventoryCloseEvent extends InventoryEvent {

	HumanEntity getPlayer();

}