package net.netcoding.nifty.common.minecraft.entity.living.monster;

import net.netcoding.nifty.common.minecraft.material.MaterialData;

/**
 * Represents an Enderman.
 */
public interface Enderman extends Monster {

	/**
	 * Get the id and data of the block that the Enderman is carrying.
	 *
	 * @return MaterialData containing the id and data of the block.
	 */
	MaterialData getCarriedMaterial();

	/**
	 * Set the id and data of the block that the Enderman is carring.
	 *
	 * @param material Data to set the carried block to.
	 */
	void setCarriedMaterial(MaterialData material);

}