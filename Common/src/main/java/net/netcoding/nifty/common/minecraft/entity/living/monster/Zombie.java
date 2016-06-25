package net.netcoding.nifty.common.minecraft.entity.living.monster;

import net.netcoding.nifty.common.minecraft.entity.living.Villager;

/**
 * Represents a Zombie.
 */
public interface Zombie extends Monster {

	/**
	 * Returns the villager profession of the zombie if the zombie is a villager.
	 *
	 * @return the profession or null.
	 */
	Villager.Profession getVillagerProfession();

	/**
	 * Gets whether the zombie is a baby.
	 *
	 * @return True if the zombie is a baby.
	 */
	boolean isBaby();

	/**
	 * Gets whether the zombie is a villager.
	 *
	 * @return True if the zombie is a villager.
	 */
	boolean isVillager();

	/**
	 * Sets whether the zombie is a baby.
	 *
	 * @param flag Whether the zombie is a baby.
	 */
	void setBaby(boolean flag);

	/**
	 * Sets whether the zombie is a villager.
	 * <p>
	 * Defaults to a {@link Villager.Profession#NORMAL}
	 *
	 * @param villager True if the zombie is a villager.
	 */
	default void setVillager(boolean villager) {
		if (villager)
			this.setVillagerProfession(Villager.Profession.NORMAL);
	}

	/**
	 * Sets whether the zombie is a villager.
	 *
	 * @param profession The profession of the villager or null to clear.
	 */
	void setVillagerProfession(Villager.Profession profession);

}