package net.netcoding.nifty.common._new_.minecraft.entity;

public interface Tameable {

	/**
	 * Check if this is tamed.
	 * <p>
	 * If something is tamed then a player can not tame it through normal
	 * methods, even if it does not belong to anyone in particular.
	 *
	 * @return True if this has been tamed.
	 */
	boolean isTamed();

	/**
	 * Sets if this has been tamed. Not necessary if the method setOwner has
	 * been used, as it tames automatically.
	 * <p>
	 * If something is tamed then a player can not tame it through normal
	 * methods, even if it does not belong to anyone in particular.
	 *
	 * @param tame True if tamed.
	 */
	void setTamed(boolean tame);

	/**
	 * Gets the current owning AnimalTamer.
	 *
	 * @return the owning AnimalTamer, or null if not owned.
	 */
	AnimalTamer getOwner();

	/**
	 * Set this to be owned by given AnimalTamer.
	 * <p>
	 * If the owner is not null, this will be tamed and will have any current
	 * path it is following removed. If the owner is set to null, this will be
	 * untamed, and the current owner removed.
	 *
	 * @param tamer The AnimalTamer who should own this.
	 */
	void setOwner(AnimalTamer tamer);

}