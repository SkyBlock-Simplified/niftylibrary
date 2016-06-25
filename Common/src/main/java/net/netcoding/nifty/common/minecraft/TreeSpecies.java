package net.netcoding.nifty.common.minecraft;

import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum TreeSpecies {

	/**
	 * Represents the common tree species.
	 */
	GENERIC(0x0),
	/**
	 * Represents the darker barked/leaved tree species.
	 */
	REDWOOD(0x1),
	/**
	 * Represents birches.
	 */
	BIRCH(0x2),
	/**
	 * Represents jungle trees.
	 */
	JUNGLE(0x3),
	/**
	 * Represents acacia trees.
	 */
	ACACIA(0x4),
	/**
	 * Represents dark oak trees.
	 */
	DARK_OAK(0x5);

	private static final ConcurrentMap<Byte, TreeSpecies> BY_DATA = new ConcurrentMap<>();
	private final byte data;

	static {
		for (TreeSpecies species : values())
			BY_DATA.put(species.getData(), species);
	}

	TreeSpecies(int data) {
		this.data = (byte)data;
	}

	public byte getData() {
		return this.data;
	}

	public static TreeSpecies getByData(byte data) {
		return BY_DATA.get(data);
	}

}