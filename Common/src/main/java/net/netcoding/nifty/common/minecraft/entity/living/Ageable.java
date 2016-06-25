package net.netcoding.nifty.common.minecraft.entity.living;

public interface Ageable extends Creature {

	/**
	 * Return the ability to breed of the entity.
	 *
	 * @return The ability to breed of the entity.
	 */
	boolean canBreed();

	/**
	 * Gets the age of this entity.
	 *
	 * @return Age.
	 */
	int getAge();

	/**
	 * Gets the current agelock.
	 *
	 * @return the current agelock.
	 */
	boolean getAgeLock();

	/**
	 * Returns true if the entity is an adult.
	 *
	 * @return True if the entity is an adult.
	 */
	boolean isAdult();

	/**
	 * Sets the age of this entity.
	 *
	 * @param age New age.
	 */
	void setAge(int age);

	/**
	 * Lock the age of the entity, setting this will prevent the entity from
	 * maturing or getting ready for mating.
	 *
	 * @param lock New lock.
	 */
	void setAgeLock(boolean lock);

	/**
	 * Sets the age of the entity to an adult.
	 */
	void setAdult();

	/**
	 * Sets the age of the entity to a baby.
	 */
	void setBaby();

	/**
	 * Set breedability of the entity, if the entity is a baby and set to
	 * breed it will instantly grow up.
	 *
	 * @param breed Breedability of the entity.
	 */
	void setBreed(boolean breed);

}