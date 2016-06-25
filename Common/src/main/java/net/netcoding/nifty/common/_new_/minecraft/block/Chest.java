package net.netcoding.nifty.common._new_.minecraft.block;

import net.netcoding.nifty.common._new_.minecraft.inventory.Inventory;
import net.netcoding.nifty.common._new_.minecraft.inventory.InventoryHolder;

public interface Chest extends BlockState, InventoryHolder {

	/**
	 * Returns the chest's inventory. If this is a double chest, it returns
	 * just the portion of the inventory linked to this half of the chest.
	 *
	 * @return The inventory.
	 */
	Inventory getBlockInventory();

}