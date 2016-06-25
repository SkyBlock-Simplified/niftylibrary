package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.inventory.item.SkullType;

public interface Skull extends BlockState {

	/**
	 * Checks to see if the skull has an owner.
	 *
	 * @return True if the skull has an owner.
	 */
	boolean hasOwner();

	/**
	 * Gets the owner of the skull, if one exists.
	 *
	 * @return The owner of the skull or null if the skull does not have an owner
	 */
	default String getOwner() {
		return this.getOwningPlayer().getName();
	}

	/**
	 * Sets the owner of the skull.
	 * <p>
	 * Involves a potentially blocking web request to acquire the profile data for
	 * the provided name.
	 *
	 * @param name The new owner of the skull.
	 * @return true If the owner was successfully set.
	 */
	boolean setOwner(String name);

	/**
	 * Get the player which owns the skull. This player may appear as the
	 * texture depending on skull type.
	 *
	 * @return Owning player.
	 */
	OfflinePlayer getOwningPlayer();

	/**
	 * Set the player which owns the skull. This player may appear as the
	 * texture depending on skull type.
	 *
	 * @param player The owning player.
	 */
	void setOwningPlayer(OfflinePlayer player);

	/**
	 * Gets the rotation of the skull in the world.
	 *
	 * @return The rotation of the skull.
	 */
	BlockFace getRotation();

	/**
	 * Sets the rotation of the skull in the world.
	 *
	 * @param rotation The rotation of the skull.
	 */
	void setRotation(BlockFace rotation);

	/**
	 * Gets the type of skull.
	 *
	 * @return The type of skull.
	 */
	SkullType getSkullType();

	/**
	 * Sets the type of skull.
	 *
	 * @param type The type of skull.
	 */
	void setSkullType(SkullType type);

}