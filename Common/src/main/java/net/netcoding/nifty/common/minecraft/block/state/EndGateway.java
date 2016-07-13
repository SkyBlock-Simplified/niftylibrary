package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.region.Location;

public interface EndGateway extends BlockState {

	/**
	 * Gets the location that entities are teleported to when
	 * entering the gateway portal.
	 *
	 * @return The gateway exit location.
	 */
	Location getExitLocation();

	/**
	 * Gets whether this gateway will teleport entities directly to
	 * the exit location instead of finding a nearby location.
	 *
	 * @return True if the gateway is teleporting to the exact location.
	 */
	boolean isExactTeleport();

	/**
	 * Sets whether this gateway will teleport entities directly to
	 * the exit location instead of finding a nearby location.
	 *
	 * @param exact Whether to teleport to the exact location.
	 */
	void setExactTeleport(boolean exact);

	/**
	 * Sets the exit location that entities are teleported to when
	 * they enter the gateway portal.
	 *
	 * @param location The new exit location.
	 */
	void setExitLocation(Location location);

}