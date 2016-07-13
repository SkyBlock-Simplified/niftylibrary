package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.event.entity.EntityDamageEvent;
import net.netcoding.nifty.common.minecraft.event.player.PlayerTeleportEvent;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.misc.Vector;
import net.netcoding.nifty.craftbukkit.minecraft.command.CraftCommandSource;
import net.netcoding.nifty.craftbukkit.minecraft.event.entity.CraftEntityDamageEvent;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftWorld;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;
import java.util.UUID;

public abstract class CraftEntity extends CraftCommandSource implements Entity {

	private final World world;

	protected CraftEntity(org.bukkit.entity.Entity entity) {
		super(entity);
		this.world = new CraftWorld(entity.getWorld());
	}

	public static Entity convertBukkitEntity(org.bukkit.entity.Entity bukkitEntity) {
		return convertBukkitEntity(bukkitEntity, Entity.class);
	}

	public static <T extends Entity> T convertBukkitEntity(org.bukkit.entity.Entity bukkitEntity, Class<T> entityType) {
		CraftEntityType type = CraftEntityType.getByBukkitClass(bukkitEntity.getClass());

		if (type != CraftEntityType.UNKNOWN)
			return entityType.cast(new Reflection(type.getEntityClass()).newInstance(bukkitEntity));

		return null;
	}

	@Override
	public boolean eject() {
		return this.getHandle().eject();
	}

	@Override
	public String getCustomName() {
		return this.getHandle().getCustomName();
	}

	@Override
	public int getEntityId() {
		return getHandle().getEntityId();
	}

	@Override
	public float getFallDistance() {
		return this.getHandle().getFallDistance();
	}

	@Override
	public int getFireTicks() {
		return this.getHandle().getFireTicks();
	}

	@Override
	public org.bukkit.entity.Entity getHandle() {
		return (org.bukkit.entity.Entity)super.getHandle();
	}

	@Override
	public EntityDamageEvent getLastDamageCause() {
		return new CraftEntityDamageEvent(this.getHandle().getLastDamageCause());
	}

	@Override
	public Location getLocation() {
		return new CraftLocation(this.getHandle().getLocation());
	}

	@Override
	public int getMaxFireTicks() {
		return this.getHandle().getMaxFireTicks();
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return this.getWorld().getNearbyEntities(this.getLocation(), x, y, z);
	}

	@Override
	public Entity getPassenger() {
		return convertBukkitEntity(this.getHandle().getPassenger());
	}

	@Override
	public int getTicksLived() {
		return this.getHandle().getTicksLived();
	}

	@Override
	public EntityType getType() {
		return EntityType.valueOf(this.getHandle().getType().name());
	}

	@Override
	public UUID getUniqueId() {
		return this.getHandle().getUniqueId();
	}

	@Override
	public Entity getVehicle() {
		return convertBukkitEntity(this.getHandle().getVehicle());
	}

	@Override
	public Vector getVelocity() {
		return CraftConverter.fromBukkitVector(this.getHandle().getVelocity());
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean hasGravity() {
		return false; // TODO
	}

	@Override
	public boolean isCustomNameVisible() {
		return this.getHandle().isCustomNameVisible();
	}

	@Override
	public boolean isDead() {
		return this.getHandle().isDead();
	}

	@Override
	public boolean isEmpty() {
		return this.getHandle().isEmpty();
	}

	@Override
	public boolean isGlowing() {
		return this.getHandle().isGlowing();
	}

	@Override
	public boolean isInsideVehicle() {
		return this.getHandle().isInsideVehicle();
	}

	@Override
	public boolean isInvulnerable() {
		return this.getHandle().isInvulnerable();
	}

	@Override
	public boolean isOnGround() {
		return this.getHandle().isOnGround();
	}

	@Override
	public boolean isSilent() {
		return this.getHandle().isSilent();
	}

	@Override
	public boolean isValid() {
		return this.getHandle().isValid();
	}

	@Override
	public boolean leaveVehicle() {
		return this.getHandle().leaveVehicle();
	}

	@Override
	public void playEffect(Effect effect) {
		this.getHandle().playEffect(org.bukkit.EntityEffect.valueOf(effect.name()));
	}

	@Override
	public void remove() {
		this.getHandle().remove();
	}

	@Override
	public void setCustomName(String name) {
		this.getHandle().setCustomName(name);
	}

	@Override
	public void setCustomNameVisible(boolean value) {
		this.getHandle().setCustomNameVisible(value);
	}

	@Override
	public void setFallDistance(float distance) {
		this.getHandle().setFallDistance(distance);
	}

	@Override
	public void setFireTicks(int ticks) {
		this.getHandle().setFireTicks(ticks);
	}

	@Override
	public void setGlowing(boolean value) {
		this.getHandle().setGlowing(value);
	}

	@Override
	public void setGravity(boolean value) {
		// TODO
	}

	@Override
	public void setInvulnerable(boolean value) {
		this.getHandle().setInvulnerable(value);
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent event) {
		this.getHandle().setLastDamageCause(((CraftEntityDamageEvent)event).getHandle());
	}

	@Override
	public boolean setPassenger(Entity passenger) {
		return this.getHandle().setPassenger(((CraftEntity)passenger).getHandle());
	}

	@Override
	public void setSilent(boolean value) {
		this.getHandle().setSilent(value);
	}

	@Override
	public void setTicksLived(int ticks) {
		this.getHandle().setTicksLived(ticks);
	}

	@Override
	public void setVelocity(Vector velocity) {
		this.getHandle().setVelocity(CraftConverter.toBukkitVector(velocity));
	}

	@Override
	public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
		return this.getHandle().teleport(((CraftLocation)location).getHandle(), org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.valueOf(cause.name()));
	}

}