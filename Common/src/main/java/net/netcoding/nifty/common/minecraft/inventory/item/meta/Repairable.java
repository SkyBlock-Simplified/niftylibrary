package net.netcoding.nifty.common.minecraft.inventory.item.meta;

/**
 * Represents an item that can be repaired at an anvil.
 */
public interface Repairable extends Cloneable {

	Repairable clone();

	/**
	 * Gets the repair penalty
	 *
	 * @return The repair penalty.
	 */
	int getRepairCost();

	/**
	 * Checks to see if this has a repair penalty.
	 *
	 * @return The if this has a repair penalty.
	 */
	boolean hasRepairCost();

	/**
	 * Sets the repair penalty
	 *
	 * @param cost Repair penalty.
	 */
	void setRepairCost(int cost);

}