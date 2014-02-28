package net.netcoding.niftybukkit.hologram.wrapper;

import java.lang.reflect.InvocationTargetException;

import net.netcoding.niftybukkit.NiftyBukkit;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;

abstract class AbstractPacket {

	protected PacketContainer handle;

	/**
	 * Constructs a new strongly typed wrapper for the given packet.
	 * @param handle - handle to the raw packet data.
	 * @param type - the packet type.
	 */
	protected AbstractPacket(PacketContainer handle, PacketType type) {
		// Make sure we're given a valid packet
		if (handle == null)
			throw new IllegalArgumentException("Packet handle cannot be NULL.");
		if (!Objects.equal(handle.getType(), type))
			throw new IllegalArgumentException(String.format("%s is not a packet of type %s", handle.getHandle(), type));

		this.handle = handle;
	}

	/**
	 * Retrieve a handle to the raw packet data.
	 * @return Raw packet data.
	 */
	public PacketContainer getHandle() {
		return handle;
	}

	/**
	 * Send the current packet to the given receiver.
	 * @param receiver - the receiver.
	 * @throws RuntimeException If the packet cannot be sent.
	 */
	public void sendPacket(Player receiver) {
		try {
			NiftyBukkit.getProtocolManager().sendServerPacket(receiver, getHandle());
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet.", e);
		}
	}

}