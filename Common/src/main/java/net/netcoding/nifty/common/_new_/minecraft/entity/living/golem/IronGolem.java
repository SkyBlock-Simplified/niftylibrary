package net.netcoding.nifty.common._new_.minecraft.entity.living.golem;

/**
 * An iron Golem that protects Villages.
 */
public interface IronGolem extends Golem {

	/**
	 * Gets whether this iron golem was built by a player.
	 *
	 * @return True if iron golem was built by a player.
	 */
	boolean isPlayerCreated();

	/**
	 * Sets whether this iron golem was built by a player or not.
	 *
	 * @param playerCreated true if you want to set the iron golem as being
	 *     player created, false if you want it to be a natural village golem.
	 */
	void setPlayerCreated(boolean playerCreated);

}