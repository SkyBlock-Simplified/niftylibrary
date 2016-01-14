package net.netcoding.niftybukkit.util;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/**
 * A collection of location utilities to assist in location
 * determining and manipulation.
 */
public class LocationUtil {

	private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
	private static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

	/**
	 * Gets the horizontal Block Face from a given yaw angle<br>
	 * This includes the NORTH_WEST faces
	 *
	 * @param yaw angle
	 * @return The Block Face of the angle
	 */
	public static BlockFace yawToFace(float yaw) {
		return yawToFace(yaw, true);
	}

	/**
	 * Gets the horizontal Block Face from a given yaw angle
	 *
	 * @param yaw angle
	 * @param useSubCardinalDirections True to allow NORTH_WEST to be returned
	 * @return The Block Face of the angle
	 */
	public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
		if (useSubCardinalDirections)
			return radial[Math.round(yaw / 45f) & 0x7];
		else
			return axis[Math.round(yaw / 90f) & 0x3];
	}

	/**
	 * Center a locations coordinates.
	 * @param location location to center
	 * @return centered location
	 */
	public static Location center(Location location) {
		Location loc = location.clone();
		loc.setX(location.getBlockX() + 0.5D);
		loc.setY(location.getBlockY() + 0.5D);
		loc.setZ(location.getBlockZ() + 0.5D);
		return loc;
	}

	/**
	 * Levels off a locations pitch.
	 * @param location location to level off
	 * @return levelled off location
	 */
	public static Location level(Location location) {
		Location loc = location.clone();
		loc.setPitch(0.0f);
		return loc;
	}

	/**
	 * Straightens a locations yaw.
	 * @param location location to straighten
	 * @return straightened location
	 */
	public static Location straighten(Location location) {
		Location loc = location.clone();
		loc.setYaw(Math.round(location.getYaw() / 90f) * 90f);
		return loc;
	}

}