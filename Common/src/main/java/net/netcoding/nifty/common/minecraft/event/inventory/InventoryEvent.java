package net.netcoding.nifty.common.minecraft.event.inventory;

import net.netcoding.nifty.common.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;

import java.util.List;

public interface InventoryEvent extends Event {

	Inventory getInventory();

	InventoryView getView();

	default List<HumanEntity> getViewers() {
		return this.getInventory().getViewers();
	}

}