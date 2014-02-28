package net.netcoding.niftybukkit.hologram.wrapper;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerAttachEntity extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.ATTACH_ENTITY;

	public WrapperPlayServerAttachEntity() {
		super(new PacketContainer(TYPE), TYPE);
		this.handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerAttachEntity(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve whether or not the entity is leached onto the vehicle.
	 * @return TRUE if it is, FALSE otherwise.
	 */
	public boolean getLeached() {
		return this.handle.getIntegers().read(0) != 0;
	}

	/**
	 * Set whether or not the entity is leached onto the vehicle.
	 * @param value - TRUE if it is leached, FALSE otherwise.
	 */
	public void setLeached(boolean value) {
		this.handle.getIntegers().write(0, value ? 1 : 0);
	}

	/**
	 * Retrieve the player entity ID being attached.
	 * @return The current Entity ID
	 */
	public int getEntityId() {
		return this.handle.getIntegers().read(1);
	}

	/**
	 * Set the player entity ID being attached.
	 * @param value - new value.
	 */
	public void setEntityId(int value) {
		this.handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve the entity being attached.
	 * @param world - the current world of the entity.
	 * @return The entity.
	 */
	public Entity getEntity(World world) {
		return this.handle.getEntityModifier(world).read(1);
	}

	/**
	 * Retrieve the entity being attached.
	 * @param event - the packet event.
	 * @return The entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Retrieve the vehicle entity ID attached to (-1 for unattaching).
	 * @return The current Vehicle ID
	 */
	public int getVehicleId() {
		return this.handle.getIntegers().read(2);
	}

	/**
	 * Set the vehicle entity ID attached to (-1 for unattaching).
	 * @param value - new value.
	 */
	public void setVehicleId(int value) {
		this.handle.getIntegers().write(2, value);
	}

	/**
	 * Retrieve the vehicle entity attached to (NULL for unattaching).
	 * @param world - the current world of the entity.
	 * @return The vehicle.
	 */
	public Entity getVehicle(World world) {
		return this.handle.getEntityModifier(world).read(2);
	}

	/**
	 * Retrieve the vehicle entity attached to (NULL for unattaching).
	 * @param event - the packet event.
	 * @return The vehicle.
	 */
	public Entity getVehicle(PacketEvent event) {
		return getVehicle(event.getPlayer().getWorld());
	}
}