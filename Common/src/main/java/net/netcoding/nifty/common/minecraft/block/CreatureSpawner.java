package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.common.minecraft.entity.EntityType;

public interface CreatureSpawner extends BlockState {

	/**
	 * Get the spawner's creature type.
	 *
	 * @return The creature type.
	 */
	EntityType getSpawnedType();

	/**
	 * Set the spawner's creature type.
	 *
	 * @param creatureType The creature type.
	 */
	void setSpawnedType(EntityType creatureType);

	/**
	 * Set the spawner mob type.
	 *
	 * @param creatureType The creature type's name.
	 */
	void setCreatureTypeByName(String creatureType);

	/**
	 * Get the spawner's creature type.
	 *
	 * @return The creature type's name.
	 */
	String getCreatureTypeName();

	/**
	 * Get the spawner's delay.
	 *
	 * @return The delay.
	 */
	int getDelay();

	/**
	 * Set the spawner's delay.
	 *
	 * @param delay The delay.
	 */
	void setDelay(int delay);

}