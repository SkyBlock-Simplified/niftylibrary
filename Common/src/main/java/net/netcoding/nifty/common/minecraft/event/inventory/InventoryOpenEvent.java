package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.Cancellable;

public interface InventoryOpenEvent extends InventoryEvent, Cancellable {

	HumanEntity getPlayer();

}