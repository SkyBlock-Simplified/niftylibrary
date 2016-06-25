package net.netcoding.nifty.common._new_.minecraft.event.inventory;

import net.netcoding.nifty.common._new_.minecraft.entity.living.HumanEntity;
import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.InventoryView;

import java.util.List;

public interface InventoryEvent {

	Inventory getInventory();

	InventoryView getView();

	default List<HumanEntity> getViewers() {
		return this.getInventory().getViewers();
	}

}