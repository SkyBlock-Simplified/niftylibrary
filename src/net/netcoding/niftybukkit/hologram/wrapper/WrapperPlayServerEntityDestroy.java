package net.netcoding.niftybukkit.hologram.wrapper;

import java.util.List;

import net.netcoding.niftybukkit.utilities.ProtocolLibNotFoundException;

import com.google.common.primitives.Ints;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {

	public static final com.comphenix.protocol.PacketType TYPE = com.comphenix.protocol.PacketType.Play.Server.ENTITY_DESTROY;

	public WrapperPlayServerEntityDestroy() throws ProtocolLibNotFoundException {
		super(new com.comphenix.protocol.events.PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerEntityDestroy(com.comphenix.protocol.events.PacketContainer packet) throws ProtocolLibNotFoundException {
		super(packet, TYPE);
	}

	/**
	 * Retrieve the IDs of the entities that will be destroyed.
	 * @return The current entities.
	 */
	public List<Integer> getEntities() {
		return Ints.asList(handle.getIntegerArrays().read(0));
	}

	/**
	 * Set the entities that will be destroyed.
	 * @param value - new value.
	 */
	public void setEntities(int[] entities) {
		handle.getIntegerArrays().write(0, entities);
	}

	/**
	 * Set the entities that will be destroyed.
	 * @param value - new value.
	 */
	public void setEntities(List<Integer> entities) {
		setEntities(Ints.toArray(entities));
	}

}