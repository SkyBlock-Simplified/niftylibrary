package net.netcoding.nifty.common.minecraft.entity.living.animal;

import net.netcoding.nifty.common.minecraft.entity.Tameable;

/**
 * Represents an Ocelot (cat).
 */
public interface Ocelot extends Animal, Tameable {

	/**
	 * Gets the current type of this cat.
	 *
	 * @return Type of the cat.
	 */
	Type getCatType();

	/**
	 * Checks if this ocelot is sitting.
	 *
	 * @return True if sitting.
	 */
	boolean isSitting();

	/**
	 * Sets the current type of this cat.
	 *
	 * @param type New type of this cat.
	 */
	void setCatType(Type type);

	/**
	 * Sets if this ocelot is sitting. Will remove any path that the ocelot was following beforehand.
	 *
	 * @param sitting True if sitting.
	 */
	void setSitting(boolean sitting);

	/**
	 * Represents the various different cat types there are.
	 */
	enum Type {

		WILD_OCELOT(0),
		BLACK_CAT(1),
		RED_CAT(2),
		SIAMESE_CAT(3);

		private static final Type[] TYPES = values();
		private final int id;

		Type(int id) {
			this.id = id;
		}

		/**
		 * Gets the ID of this cat type.
		 *
		 * @return Type ID.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets a cat type by its ID.
		 *
		 * @param id ID of the cat type to get.
		 * @return Resulting type, or null if not found.
		 */
		public static Type getTypeById(int id) {
			return (id >= TYPES.length) ? null : TYPES[id];
		}

	}

}