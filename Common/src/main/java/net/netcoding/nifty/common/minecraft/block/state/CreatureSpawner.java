package net.netcoding.nifty.common.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.entity.EntityType;

public interface CreatureSpawner extends BlockState {

	/**
	 * Get the spawner's creature type.
	 *
	 * @return The creature type's name.
	 */
	default String getCreatureTypeName() {
		return this.getSpawnedType().getName();
	}

	/**
	 * Get the spawner's delay.
	 *
	 * @return The delay.
	 */
	int getDelay();

	/**
	 * Get the spawner's creature type.
	 *
	 * @return The creature type.
	 */
	EntityType getSpawnedType();

	/**
	 * Set the spawner mob type.
	 *
	 * @param creatureType The creature type's name.
	 */
	default void setCreatureTypeByName(String creatureType) {
		EntityType type = EntityType.getByName(creatureType);

		if (type != null)
			this.setSpawnedType(type);
	}

	/**
	 * Set the spawner's delay.
	 *
	 * @param delay The delay.
	 */
	void setDelay(int delay);

	/**
	 * Set the spawner's creature type.
	 *
	 * @param creatureType The creature type.
	 */
	void setSpawnedType(EntityType creatureType);

}