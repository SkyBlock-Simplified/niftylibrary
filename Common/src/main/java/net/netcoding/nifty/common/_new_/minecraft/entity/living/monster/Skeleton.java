package net.netcoding.nifty.common._new_.minecraft.entity.living.monster;

public interface Skeleton extends Monster {

	/**
	 * Gets the type of skeleton.
	 *
	 * @return The current type.
	 */
	Type getSkeletonType();

	/**
	 * Sets the new type of skeleton.
	 *
	 * @param type The new type.
	 */
	void setSkeletonType(Type type);

	/**
	 * Represents the various types of a Skeleton.
	 */
	enum Type {

		/**
		 * Standard skeleton type.
		 */
		NORMAL,
		/**
		 * Wither skeleton. Generally found in Nether fortresses.
		 */
		WITHER,
		/**
		 * Stray skeleton. Generally found in ice biomes. Shoots tipped arrows.
		 */
		STRAY

	}

}