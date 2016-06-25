package net.netcoding.nifty.common.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.material.Material;

/**
 * Represents a skull ({@link Material#SKULL_ITEM}) that can have an owner.
 */
public interface SkullMeta extends ItemMeta {

	@Override
	SkullMeta clone();

	/**
	 * Gets the owner of the skull.
	 *
	 * @return The owner if the skull.
	 */
	String getOwner();

	/**
	 * Checks to see if the skull has an owner.
	 *
	 * @return True if the skull has an owner.
	 */
	boolean hasOwner();

	/**
	 * Sets the owner of the skull.
	 * <p>
	 * Should check that hasOwner() returns true before calling this plugin.
	 *
	 * @param owner the new owner of the skull
	 * @return true if the owner was successfully set
	 */
	boolean setOwner(String owner);

}