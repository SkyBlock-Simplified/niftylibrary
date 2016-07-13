package net.netcoding.nifty.common.minecraft.entity.living;

import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Damageable;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.living.animal.Animal;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.entity.projectile.source.ProjectileSource;
import net.netcoding.nifty.common.minecraft.inventory.EntityEquipment;
import net.netcoding.nifty.common.minecraft.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.region.Location;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface LivingEntity extends Damageable, ProjectileSource {

	/**
	 * Adds the given {@link PotionEffect} to the living entity.
	 *
	 * @param effect Effect to be added.
	 * @return True if the effect could be added.
	 */
	default boolean addPotionEffect(PotionEffect effect) {
		return this.addPotionEffect(effect, false);
	}

	/**
	 * Adds the given {@link PotionEffect} to the living entity.
	 *
	 * @param effect Effect to be added.
	 * @param force Whether conflicting effects should be removed.
	 * @return True if the effect could be added.
	 */
	boolean addPotionEffect(PotionEffect effect, boolean force);

	default boolean addPotionEffects(Collection<PotionEffect> effects) {
		boolean change = false;

		for (PotionEffect effect : effects)
			change = change || this.addPotionEffect(effect);

		return change;
	}

	/**
	 * Returns all currently active potion effects on the living entity.
	 *
	 * @return All active potion effects.
	 */
	Collection<PotionEffect> getActivePotionEffects();

	/**
	 * Gets if this living entity can pick up items.
	 *
	 * @return True if can pickup items.
	 */
	boolean getCanPickupItems();

	/**
	 * Gets the inventory with the equipment worn by the living entity.
	 *
	 * @return The living entity's inventory.
	 */
	EntityEquipment getEquipment();

	/**
	 * Gets the height of the living entity's eyes above its Location
	 *
	 * @return The height of this living entity's eye.
	 */
	default double getEyeHeight() {
		return this.getEyeHeight(false);
	}

	/**
	 * Gets the height of the living entity's eyes above its Location
	 *
	 * @param ignoreSneaking Whether to ignore the effects of sneaking.
	 * @return The height of this living entity's eye.
	 */
	double getEyeHeight(boolean ignoreSneaking);

	/**
	 * Gets a location detailing the current eye position of this living entity.
	 *
	 * @return The location at the eyes.
	 */
	Location getEyeLocation();

	/**
	 * Gets the player identified as the killer of the living entity.
	 *
	 * @return Killing player, or null.
	 */
	Player getKiller();

	/**
	 * Gets the living entity's last damage taken in the current no damage ticks time.
	 * <p>
	 * Only damage higher than this amount will further damage the living entity.
	 *
	 * @return The damage taken since last no damage ticks period.
	 */
	double getLastDamage();

	/**
	 * Gets the last two blocks along this living entity's line of sight.
	 * <p>
	 * The target block will be the last block in the list.
	 *
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing last two blocks along this living entity's line of sight.
	 */
	default List<Block> getLastTwoTargetBlocks(int distance) {
		return this.getLastTwoTargetBlocks(null, distance);
	}

	/**
	 * Gets the last two blocks along this living entity's line of sight.
	 * <p>
	 * The target block will be the last block in the list.
	 *
	 * @param materials All block types to ignore, null for air.
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing last two blocks along this living entity's line of sight.
	 */
	List<Block> getLastTwoTargetBlocks(Set<Material> materials, int distance);

	Entity getLeashHolder();

	/**
	 * Gets all blocks along this living entity's line of sight.
	 * This list contains all blocks from this living entity's eye position to target inclusive.
	 *
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing all blocks along this living entity's line of sight.
	 */
	default List<Block> getLineOfSight(int distance) {
		return this.getLineOfSight(null, distance);
	}

	/**
	 * Gets all blocks along this living entity's line of sight.
	 * This list contains all blocks from the living entity's eye position to target inclusive.
	 *
	 * @param materials All block types to ignore, null for air.
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing all blocks along this living entity's line of sight.
	 */
	List<Block> getLineOfSight(Set<Material> materials, int distance);

	/**
	 * Gets the maximum amount of air this living entity has (in ticks).
	 *
	 * @return The maximum amount of air.
	 */
	int getMaximumAir();

	/**
	 * Gets the living entity's current maximum no damage ticks.
	 * <p>
	 * This is the maximum duration in which the living entity will not take damage.
	 *
	 * @return The maximum no damange ticks.
	 */
	int getMaximumNoDamageTicks();

	/**
	 * Gets the living entity's current no damage ticks.
	 *
	 * @return The amount of no damage ticks.
	 */
	int getNoDamageTicks();

	/**
	 * Gets the amount of air that the living entity has remaining (in ticks).
	 *
	 * @return The amount of air remaining.
	 */
	int getRemainingAir();

	/**
	 * Returns if the living entity despawns when away from players.
	 * <p>
	 * By default, {@link Animal Animals} are not removed, but other mobs are.
	 *
	 * @return True if this living entity is removed when away from players.
	 */
	boolean getRemoveWhenFarAway();

	/**
	 * Gets the block at the end of this living entity's line of sight.
	 * This list contains all blocks from the living entity's eye position to target inclusive.
	 *
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing all blocks along this living entity's line of sight.
	 */
	default Block getTargetBlock(int distance) {
		return this.getTargetBlock(null, distance);
	}

	/**
	 * Gets the block at the end of this living entity's line of sight.
	 * This list contains all blocks from the living entity's eye position to target inclusive.
	 *
	 * @param materials All block types to ignore, null for air.
	 * @param distance The maximum distance to scan. (This may be limited by the server,
	 *                 but never to less than 100 blocks.)
	 * @return List containing all blocks along this living entity's line of sight.
	 */
	Block getTargetBlock(Set<Material> materials, int distance);

	/**
	 * Checks if this living entity has AI.
	 *
	 * @return True if this living entity has AI.
	 */
	boolean hasAI();

	/**
	 * Checks if the living entity has block line of sight to another.
	 * <p>
	 * This uses the same algorithm that hostile mobs use to find the closest player.
	 *
	 * @param entity Entity to check line of sight with.
	 * @return True if there is a line of sight.
	 */
	boolean hasLineOfSight(Entity entity);

	/**
	 * Checks if the living entity already has an existing potion effect type.
	 *
	 * @param effectType The effect type to check.
	 * @return True if this living entity has this potion effect type.
	 */
	default boolean hasPotionEffect(PotionEffectType effectType) {
		return this.getActivePotionEffects().stream().anyMatch(effect -> effect.getType().getName().equals(effectType.getName()));
	}

	/**
	 * Gets if this living entity is subject to collisions with other entities.
	 * <p>
	 * Please note that this method returns only the custom collidable state,
	 * not whether the entity is non-collidable for other reasons such as being dead.
	 *
	 * @return True if this living entity collides with other entities.
	 */
	boolean isCollidable();

	/**
	 * Checks if this living entity is gliding, such as using an Elytra.
	 *
	 * @return True if this living entity is gliding.
	 */
	boolean isGliding();

	/**
	 * Checks if this living entity is currently leashed.
	 *
	 * @return True if this living entity is leashed.
	 */
	boolean isLeashed();

	/**
	 * Removes any effects present of the given potion effect type.
	 *
	 * @param effectType The potion effect type to remove.
	 */
	void removePotionEffect(PotionEffectType effectType);

	/**
	 * Sets whether this living entity will have AI.
	 *
	 * @param value Whether the living entity will have AI.
	 */
	void setAI(boolean value);

	/**
	 * Sets whether this living entity can pick up items.
	 *
	 * @param value Whether this living entity picks up items.
	 */
	void setCanPickupItems(boolean value);

	/**
	 * Set if this living entity will be subject to collisions other entities.
	 * <p>
	 * Note that collisions are bidirectional, so this method would need to be set to false
	 * on both the collidee and the collidant to ensure no collisions take place.
	 *
	 * @param value Whether this entity should collide with other living entities.
	 */
	void setCollidable(boolean value);

	/**
	 * Makes entity start or stop gliding.
	 * <p>
	 * This will work even if an Elytra is not equipped, but will be reverted by the server
	 * immediately after unless an event-cancelling mechanism is put in place.
	 *
	 * @param value Whether the entity should glide.
	 */
	void setGliding(boolean value);

	/**
	 * Sets the damage dealt within the current no damage ticks time period.
	 *
	 * @param value The amount of damage.
	 */
	void setLastDamage(double value);

	/**
	 * Sets the leash on this living entity to be held by the supplied entity.
	 * <p>
	 * This method has no effect on EnderDragons, Withers, Players, or Bats.
	 * Non-living entities excluding leashes will not persist as leash holders.
	 *
	 * @param entity The entity to leash this entity to.
	 * @return True if the operation was successful.
	 */
	boolean setLeashHolder(Entity entity);

	/**
	 * Sets the maximum amount of air this living entity can have.
	 *
	 * @param ticks The maximum amount of air.
	 */
	void setMaximumAir(int ticks);

	/**
	 * Sets this living entity's current maximum no damage ticks.
	 *
	 * @param ticks The maximum amount of no damage ticks.
	 */
	void setMaximumNoDamageTicks(int ticks);

	/**
	 * Sets this living entity's current no damage ticks.
	 *
	 * @param ticks The amount of no damage ticks.
	 */
	void setNoDamageTicks(int ticks);

	/**
	 * Sets the amount of air that this living entity has remaining.
	 *
	 * @param ticks The amount of air remaining.
	 */
	void setRemainingAir(int ticks);

	/**
	 * Sets if this living entity despawns when away from players.
	 *
	 * @param value Whether to remove this living entity when away from players.
	 */
	void setRemoveWhenFarAway(boolean value);

}