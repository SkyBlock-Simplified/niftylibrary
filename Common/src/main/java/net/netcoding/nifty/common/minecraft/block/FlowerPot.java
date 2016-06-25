package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.common.minecraft.material.MaterialData;

public interface FlowerPot extends BlockState {

	/**
	 * Gets the item present in this flower pot.
	 *
	 * @return Item present, or null for empty.
	 */
	MaterialData getContents();

	/**
	 * Sets the item present in this flower pot.
	 *
	 * NOTE: The Vanilla Minecraft client will currently not refresh this until
	 * a block update is triggered.
	 *
	 * @param data New item, or null for empty.
	 */
	void setContents(MaterialData data);

}