package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.material.Material;

public interface Jukebox extends BlockState {

	/**
	 * Stop the jukebox playing and eject the current record.
	 *
	 * @return True if a record was ejected; false if there was none playing.
	 */
	boolean eject();

	/**
	 * Get the record currently playing.
	 *
	 * @return The record Material, or AIR if none is playing.
	 */
	Material getPlaying();

	/**
	 * Check if the jukebox is currently playing a record.
	 *
	 * @return True if there is a record playing.
	 */
	boolean isPlaying();

	/**
	 * Set the record currently playing.
	 *
	 * @param record The record Material, or null/AIR to stop playing.
	 */
	void setPlaying(Material record);

}