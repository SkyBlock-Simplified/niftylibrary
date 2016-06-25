package net.netcoding.nifty.common.minecraft.entity.block;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.complex.EnderDragon;
import net.netcoding.nifty.common.minecraft.region.Location;

/**
 * A crystal that heals nearby {@link EnderDragon EnderDragons}.
 */
public interface EnderCrystal extends Entity {

	/**
	 * Gets the location that this endercrystal is pointing its beam to.
	 *
	 * @return The location that the beam is pointed to, or null if the beam is not shown.
	 */
	Location getBeamTarget();

	/**
	 * Gets if the endercrystal is showing the bedrock slate underneath it.
	 *
	 * @return True if the bottom is being shown.
	 */
	boolean isShowingBottom();

	/**
	 * Sets the location that this end crystal is pointing to.
	 * <p>
	 * Passing a null value will remove the current beam.
	 *
	 * @param location The location to point the beam to.
	 */
	void setBeamTarget(Location location);

	/**
	 * Sets if this endercrystal is showing the bedrock slate underneath it.
	 *
	 * @param showing whether the bedrock slate should be shown
	 */
	void setShowingBottom(boolean showing);

}