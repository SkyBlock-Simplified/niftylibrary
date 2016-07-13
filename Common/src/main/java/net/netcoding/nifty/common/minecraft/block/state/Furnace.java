package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.inventory.types.FurnaceInventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;

public interface Furnace extends BlockState, InventoryHolder {

	/**
	 * Get burn time.
	 *
	 * @return Burn time.
	 */
	short getBurnTime();

	/**
	 * Get cook time.
	 *
	 * @return Cook time.
	 */
	short getCookTime();

	@Override
	FurnaceInventory getInventory();

	/**
	 * Set burn time.
	 *
	 * @param value Burn time.
	 */
	void setBurnTime(short value);

	/**
	 * Set cook time.
	 *
	 * @param value Cook time.
	 */
	void setCookTime(short value);

}