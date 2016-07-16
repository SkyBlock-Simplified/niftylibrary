package net.netcoding.nifty.common.minecraft.entity.vehicle.minecart;

import net.netcoding.nifty.common.minecraft.entity.vehicle.Vehicle;
import net.netcoding.nifty.common.minecraft.material.MaterialData;
import net.netcoding.nifty.core.util.misc.Vector;

/**
 * Represents a minecart entity.
 */
public interface Minecart extends Vehicle {

	/**
	 * Gets a minecart's damage.
	 *
	 * @return The damage.
	 */
	double getDamage();

	/**
	 * Gets the derailed velocity modifier. Used for minecarts that are on the
	 * ground, but not on rails.
	 * <p>
	 * A derailed minecart's velocity is multiplied by this factor each tick.
	 *
	 * @return Derailed visible speed.
	 */
	Vector getDerailedVelocityMod();

	/**
	 * Gets the display block for this minecart.
	 * This function will return the type AIR if none is set.
	 *
	 * @return The block displayed by this minecart.
	 */
	MaterialData getDisplayBlock();

	/**
	 * Gets the offset of the display block.
	 *
	 * @return The current block offset for this minecart.
	 */
	int getDisplayBlockOffset();

	/**
	 * Gets the flying velocity modifier. Used for minecarts that are in
	 * mid-air. A flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @return The vector factor.
	 */
	Vector getFlyingVelocityMod();

	/**
	 * Gets the maximum speed of a minecart. The speed is unrelated to the velocity.
	 *
	 * @return The max speed.
	 */
	double getMaxSpeed();

	/**
	 * Returns whether this minecart will slow down faster without a passenger occupying it.
	 *
	 * @return Whether it decelerates faster.
	 */
	boolean isSlowWhenEmpty();

	/**
	 * Sets a minecart's damage.
	 *
	 * @param damage Over 40 to "kill" a minecart
	 */
	void setDamage(double damage);

	/**
	 * Sets the derailed velocity modifier. Used for minecarts that are on the
	 * ground, but not on rails. A derailed minecart's velocity is multiplied
	 * by this factor each tick.
	 *
	 * @param derailed visible speed.
	 */
	void setDerailedVelocityMod(Vector derailed);

	/**
	 * Sets the display block for this minecart.
	 * Passing a null value will set the minecart to have no display block.
	 *
	 * @param data The material data to set as display block.
	 */
	void setDisplayBlock(MaterialData data);

	/**
	 * Sets the offset of the display block.
	 *
	 * @param offset The block offset to set for this minecart.
	 */
	void setDisplayBlockOffset(int offset);

	/**
	 * Sets the flying velocity modifier. Used for minecarts that are in
	 * mid-air. A flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @param flying Velocity modifier vector.
	 */
	void setFlyingVelocityMod(Vector flying);

	/**
	 * Sets the maximum speed of a minecart. Must be nonnegative. Default is 0.4D.
	 *
	 * @param speed The max speed.
	 */
	void setMaxSpeed(double speed);

	/**
	 * Sets whether this minecart will value down faster without a passenger occupying it
	 *
	 * @param value Whether it will decelerate faster.
	 */
	void setSlowWhenEmpty(boolean value);

}