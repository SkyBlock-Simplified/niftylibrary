package net.netcoding.nifty.common._new_.minecraft.entity.living.animal;

/**
 * Represents a Rabbit.
 */
public interface Rabbit extends Animal {

	/**
	 * Gets the type of rabbit.
	 *
	 * @return The type of rabbit.
	 */
	Type getRabbitType();

	/**
	 * Sets the new type rabbit.
	 *
	 * @param type The type of rabbit for this entity.
	 */
	void setRabbitType(Type type);

	/**
	 * Represents the various types of a Rabbit.
	 */
	enum Type {

		/**
		 * Chocolate colored rabbit.
		 */
		BROWN,
		/**
		 * Pure white rabbit.
		 */
		WHITE,
		/**
		 * Black rabbit.
		 */
		BLACK,
		/**
		 * Black with white patches, or white with black patches?
		 */
		BLACK_AND_WHITE,
		/**
		 * Golden bunny.
		 */
		GOLD,
		/**
		 * Salt and pepper colored, whatever that means.
		 */
		SALT_AND_PEPPER,
		/**
		 * Rabbit with pure white fur, blood red horizontal eyes, and is hostile to players.
		 */
		THE_KILLER_BUNNY

	}

}