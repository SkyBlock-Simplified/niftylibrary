package net.netcoding.nifty.common.minecraft;

import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum GrassSpecies {

	/**
	 * Represents the dead looking grass.
	 */
	DEAD(0x0),
	/**
	 * Represents the normal grass species.
	 */
	NORMAL(0x1),
	/**
	 * Represents the fern-looking grass species.
	 */
	FERN_LIKE(0x2);

	private static final ConcurrentMap<Byte, GrassSpecies> BY_DATA = Concurrent.newMap();
	private final byte data;

	static {
		for (GrassSpecies grassSpecies : values())
			BY_DATA.put(grassSpecies.getData(), grassSpecies);
	}

	GrassSpecies(int data) {
		this.data = (byte)data;
	}

	public byte getData() {
		return data;
	}

	public static GrassSpecies getByData(byte data) {
		return BY_DATA.get(data);
	}

}