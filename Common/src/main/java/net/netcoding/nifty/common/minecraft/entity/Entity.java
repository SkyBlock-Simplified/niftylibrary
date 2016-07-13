package net.netcoding.nifty.common.minecraft.entity;

import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.event.entity.EntityDamageEvent;
import net.netcoding.nifty.common.minecraft.event.player.PlayerTeleportEvent;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.misc.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Represents an Entity.
 */
public interface Entity extends CommandSource {

	/**
	 * Eject any passenger.
	 *
	 * @return True if there was a passenger.
	 */
	boolean eject();

	/**
	 * Gets the custom name on a mob. If there is no name this method will
	 * return null.
	 * <p>
	 * This value has no effect on players, they will always use their real name.
	 *
	 * @return The name of the mob, or null.
	 */
	String getCustomName();

	/**
	 * Gets the unique id for this entity.
	 *
	 * @return Unique id.
	 */
	int getEntityId();

	/**
	 * Gets the distance this entity has fallen.
	 *
	 * @return The fall distance.
	 */
	float getFallDistance();

	/**
	 * Gets the entity's current fire ticks (ticks before the entity stops
	 * being on fire).
	 *
	 * @return How much longer this entity will burn.
	 */
	int getFireTicks();

	/**
	 * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
	 * This event may have been cancelled.
	 *
	 * @return The last known {@link EntityDamageEvent}, or null if (until now) unharmed.
	 */
	EntityDamageEvent getLastDamageCause();

	/**
	 * Gets the entity's current position.
	 *
	 * @return A copied location of the position of this entity.
	 */
	Location getLocation();

	/**
	 * Stores the entity's current position in the provided Location object.
	 * <p>
	 * If the provided Location is null this method does nothing and returns null.
	 *
	 * @param location The location to copy into.
	 * @return The provided location object, or null.
	 */
	default Location getLocation(Location location) {
		if (location == null)
			return null;

		Location old = this.getLocation();
		location.setPitch(old.getPitch());
		location.setX(old.getX());
		location.setY(old.getY());
		location.setYaw(old.getYaw());
		location.setZ(old.getZ());
		location.setWorld(old.getWorld());
		return old;
	}

	/**
	 * Gets entity's maximum fire ticks.
	 *
	 * @return Total time this entity will burn.
	 */
	int getMaxFireTicks();

	/**
	 * Returns a list of entities within a bounding box centered around this entity.
	 *
	 * @param x 1/2 the size of the box along x axis
	 * @param y 1/2 the size of the box along y axis
	 * @param z 1/2 the size of the box along z axis
	 * @return A list of nearby entities.
	 */
	List<Entity> getNearbyEntities(double x, double y, double z);

	/**
	 * Gets the primary passenger of this entity. If this entity can have
	 * multiple passengers, this will only return the primary passenger.
	 *
	 * @return The passenger, or null if no passengers.
	 */
	Entity getPassenger();

	/**
	 * Gets the amount of ticks this entity has lived for.
	 * <p>
	 * This is the equivalent to "age" in entities.
	 *
	 * @return The age of entity.
	 */
	int getTicksLived();

	/**
	 * Get the type of the entity.
	 *
	 * @return The entity type.
	 */
	EntityType getType();

	/**
	 * Gets the unique (and persistent) id for this entity.
	 *
	 * @return Unique (and persistent) id.
	 */
	UUID getUniqueId();

	/**
	 * Get the vehicle that this player is inside.
	 *
	 * @return The current vehicle, or null.
	 */
	Entity getVehicle();

	/**
	 * Gets this entity's current velocity.
	 *
	 * @return Current travelling velocity of this entity.
	 */
	Vector getVelocity();

	/**
	 * Gets the current world this entity resides in.
	 *
	 * @return This entity's world.
	 */
	World getWorld();

	/**
	 * Gets if gravity applies to this entity.
	 *
	 * @return True if gravity applies.
	 */
	boolean hasGravity();

	/**
	 * Gets whether or not the entity's custom name is displayed client side.
	 * <p>
	 * This value has no effect on players, they will always display their name.
	 *
	 * @return True if the custom name is displayed.
	 */
	boolean isCustomNameVisible();

	/**
	 * Gets if this entity has been marked for removal.
	 *
	 * @return True if dead.
	 */
	boolean isDead();

	/**
	 * Check if this entity has passengers.
	 *
	 * @return True if the entity has no passengers.
	 */
	boolean isEmpty();

	boolean isGlowing();

	/**
	 * Returns whether this entity is inside a vehicle.
	 *
	 * @return True if the entity is in a vehicle.
	 */
	boolean isInsideVehicle();

	/**
	 * Gets if the entity is invulnerable or not.
	 *
	 * @return True if the entity is invulnerable.
	 */
	boolean isInvulnerable();

	/**
	 * Gets if the entity is supported by a block. This value is a
	 * state updated by the server and is not recalculated unless the entity
	 * moves.
	 *
	 * @return True if entity is on a block.
	 */
	boolean isOnGround();

	/**
	 * Gets if the entity is silent or not.
	 * <p>
	 * When an entity is silent it will not produce any sound.
	 *
	 * @return True if the entity is silent.
	 */
	boolean isSilent();

	/**
	 * Gets if the entity has not died or been despawned for some other reason.
	 *
	 * @return True if valid/alive.
	 */
	boolean isValid();

	/**
	 * Leave the current vehicle. If the entity is currently in a vehicle (and
	 * is removed from it), true will be returned, otherwise false will be returned.
	 *
	 * @return True if the entity was removed from a vehicle.
	 */
	boolean leaveVehicle();

	/**
	 * Performs the specified {@link Entity.Effect} for this entity.
	 * <p>
	 * This will be viewable to all players near the entity.
	 *
	 * @param effect The effect to play.
	 */
	void playEffect(Effect effect);

	/**
	 * Mark the entity for removal.
	 */
	void remove();

	/**
	 * Sets the custom name of this entity. This name will be used in death messages
	 * and can be sent to the client as a nameplate over the mob.
	 * <p>
	 * Setting the name to null or an empty string will clear it.
	 * <p>
	 * This value has no effect on players, they will always use their real name.
	 *
	 * @param name The name to set.
	 */
	void setCustomName(String name);

	/**
	 * Sets whether or not to display the entity's custom name client side. The
	 * name will be displayed above the mob similarly to a player.
	 * <p>
	 * This value has no effect on players, they will always display their name.
	 *
	 * @param value Whether to display custom name or not.
	 */
	void setCustomNameVisible(boolean value);

	/**
	 * Sets the fall distance for this entity
	 *
	 * @param distance The new fall distance.
	 */
	void setFallDistance(float distance);

	/**
	 * Sets the remaining ticks before the entity stops burning.
	 *
	 * @param ticks How long to burn the entity.
	 */
	void setFireTicks(int ticks);

	/**
	 * Sets if the entity has a team colored (default: white) glow.
	 *
	 * @param value Whether the entity is glowing.
	 */
	void setGlowing(boolean value);

	/**
	 * Sets if gravity applies to this entity.
	 *
	 * @param value Whether gravity should apply.
	 */
	void setGravity(boolean value);

	/**
	 * Sets if the entity is invulnerable or not.
	 * <p>
	 * When an entity is invulnerable it can only be damaged by players in creative mode.
	 *
	 * @param value Whether the entity is invulnerable.
	 */
	void setInvulnerable(boolean value);

	/**
	 * Record the last {@link EntityDamageEvent} inflicted on this entity
	 *
	 * @param event The most recent {@link EntityDamageEvent}.
	 */
	void setLastDamageCause(EntityDamageEvent event);

	/**
	 * Set the passenger of this entity.
	 *
	 * @param passenger The new passenger.
	 * @return False if it could not be done for whatever reason.
	 */
	boolean setPassenger(Entity passenger);

	/**
	 * Sets whether the entity is silent or not.
	 * <p>
	 * When an entity is silent it will not produce any sound.
	 *
	 * @param value Whether the entity is silent.
	 */
	void setSilent(boolean value);

	/**
	 * Sets the amount of ticks this entity has lived for.
	 * <p>
	 * This is the equivalent to "age" in entities. May not be less than one tick.
	 *
	 * @param ticks The age of this entity.
	 */
	void setTicksLived(int ticks);

	/**
	 * Sets this entity's velocity.
	 *
	 * @param velocity The new velocity to travel with.
	 */
	void setVelocity(Vector velocity);

	/**
	 * Teleports this entity to the target Entity. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param entity The entity to teleport this entity to.
	 * @return True if the teleport was successful.
	 */
	default boolean teleport(Entity entity) {
		return this.teleport(entity.getLocation());
	}

	/**
	 * Teleports this entity to the target Entity. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param entity The entity to teleport this entity to.
	 * @param cause The cause of this teleportation.
	 * @return True if the teleport was successful.
	 */
	default boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) {
		return this.teleport(entity.getLocation(), cause);
	}

	/**
	 * Teleports this entity to the given location. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param location The location to teleport this entity to.
	 * @return True if the teleport was successful.
	 */
	default boolean teleport(Location location) {
		return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
	}

	/**
	 * Teleports this entity to the given location. If this entity is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param location The location to teleport this entity to.
	 * @param cause The cause of this teleportation.
	 * @return True if the teleport was successful.
	 */
	boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause);

	enum Effect {

		HURT(2),
		DEATH(3),
		WOLF_SMOKE(6),
		WOLF_HEARTS(7),
		WOLF_SHAKE(8),
		SHEEP_EAT(10),
		IRON_GOLEM_ROSE(11),
		VILLAGER_HEART(12),
		VILLAGER_ANGRY(13),
		VILLAGER_HAPPY(14),
		WITCH_MAGIC(15),
		ZOMBIE_TRANSFORM(16),
		FIREWORK_EXPLODE(17);

		private static final ConcurrentMap<Byte, Effect> BY_DATA = Concurrent.newMap();
		private final byte data;

		static {
			Arrays.stream(values()).forEach(effect -> BY_DATA.put(effect.getData(), effect));
		}

		Effect(int data) {
			this.data = (byte)data;
		}

		public byte getData() {
			return this.data;
		}

		public static Effect getByData(byte data) {
			return BY_DATA.get(data);
		}

	}

}