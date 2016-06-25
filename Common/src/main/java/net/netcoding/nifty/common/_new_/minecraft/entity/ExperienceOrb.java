package net.netcoding.nifty.common._new_.minecraft.entity;

/**
 * Represents an Experience Orb.
 */
public interface ExperienceOrb extends Entity {

	/**
	 * Gets how much experience is contained within this orb
	 *
	 * @return Amount of experience.
	 */
	int getExperience();

	/**
	 * Sets how much experience is contained within this orb
	 *
	 * @param experience Amount of experience.
	 */
	void setExperience(int experience);

}