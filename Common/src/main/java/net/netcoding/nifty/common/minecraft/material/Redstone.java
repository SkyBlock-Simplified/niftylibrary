package net.netcoding.nifty.common.minecraft.material;

/**
 * Indicates a Material that may carry or create a Redstone current
 */
public interface Redstone {

	/**
	 * Gets the current state of this Material, indicating
	 * if it's powered or unpowered.
	 *
	 * @return True if powered.
	 */
	boolean isPowered();

}