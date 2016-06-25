package net.netcoding.nifty.common._new_.minecraft.entity;

import net.netcoding.nifty.common._new_.minecraft.event.entity.EntityDamageEvent;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerTeleportEvent;
import net.netcoding.nifty.common._new_.minecraft.region.Location;
import net.netcoding.nifty.common._new_.minecraft.region.World;
import net.netcoding.nifty.common._new_.minecraft.command.source.CommandSource;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.misc.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// TODO: https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/entity
public interface Entity extends CommandSource {

	boolean eject();

	String getCustomName();

	int getEntityId();

	float getFallDistance();

	int getFireTicks();

	EntityDamageEvent getLastDamageCause();

	Location getLocation();

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

	int getMaxFireTicks();

	List<Entity> getNearbyEntities(double x, double y, double z);

	Entity getPassenger();

	int getTicksLived();

	EntityType getType();

	UUID getUniqueId();

	Entity getVehicle();

	Vector getVelocity();

	World getWorld();

	boolean isCustomNameVisible();

	boolean isDead();

	boolean isEmpty();

	boolean isGlowing();

	boolean isInsideVehicle();

	boolean isInvulnerable();

	boolean isOnGround();

	boolean isValid();

	boolean leaveVehicle();

	void playEffect(Effect effect);

	void remove();

	void setCustomName(String name);

	void setCustomNameVisible(boolean value);

	void setFallDistance(float distance);

	void setFireTicks(int ticks);

	void setGlowing(boolean value);

	void setInvulnerable(boolean value);

	void setLastDamageCause(EntityDamageEvent event);

	boolean setPassenger(Entity passenger);

	void setTicksLived(int ticks);

	void setVelocity(Vector velocity);

	boolean teleport(Location location);

	boolean teleport(Location location, PlayerTeleportEvent.TeleportCause var2);

	boolean teleport(Entity entity);

	boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause var2);

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

		private static final ConcurrentMap<Byte, Effect> BY_DATA = new ConcurrentMap<>();
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