package net.netcoding.nifty.common._new_.minecraft.entity.explosive;

import net.netcoding.nifty.common._new_.minecraft.entity.Entity;
import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.common._new_.minecraft.region.World;

/**
 * Represents a primed TNT entity.
 */
public interface PrimedTNT extends Explosive {

	/**
	 * Set the number of ticks until the TNT blows up after being primed.
	 *
	 * @param fuseTicks The fuse ticks.
	 */
	void setFuseTicks(int fuseTicks);

	/**
	 * Retrieve the number of ticks until the explosion of this entity.
	 *
	 * @return The number of ticks until this entity explodes.
	 */
	int getFuseTicks();

	/**
	 * Gets the source of this primed TNT. The source is the entity
	 * responsible for the creation of this primed TNT. (I.E. player ignites
	 * TNT with flint and steel.) Take note that this can be null if there is
	 * no suitable source. (created by the {@link
	 * World#spawn(Location, Class)} method, for example.)
	 * <p>
	 * The source will become null if the chunk this primed TNT is in is
	 * unloaded then reloaded. If the source Entity becomes invalidated for
	 * any reason, such being removed from the world, the returned value will
	 * be null.
	 *
	 * @return The source of this primed TNT.
	 */
	Entity getSource();

}