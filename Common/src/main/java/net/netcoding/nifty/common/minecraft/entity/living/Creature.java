package net.netcoding.nifty.common.minecraft.entity.living;

/**
 * Represents a Creature. Creatures are non-intelligent monsters or animals
 * which have very simple abilities.
 */
public interface Creature extends LivingEntity {

	/**
	 * Gets the current target of this Creature.
	 *
	 * @return Current target of this creature, or null if none exists.
	 */
	LivingEntity getTarget();

	/**
	 * Instructs this Creature to set the specified LivingEntity as its
	 * target.
	 * <p>
	 * Hostile creatures may attack their target, and friendly creatures may
	 * follow their target.
	 *
	 * @param target New LivingEntity to target, or null to clear the target.
	 */
	void setTarget(LivingEntity target);

}