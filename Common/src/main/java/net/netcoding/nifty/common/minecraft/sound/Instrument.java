package net.netcoding.nifty.common.minecraft.sound;

import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum Instrument {

	/**
	 * Piano is the standard instrument for a note block.
	 */
	PIANO(0x0),
	/**
	 * Bass drum is normally played when a note block is on top of a
	 * stone-like block
	 */
	BASS_DRUM(0x1),
	/**
	 * Snare drum is normally played when a note block is on top of a sandy
	 * block.
	 */
	SNARE_DRUM(0x2),
	/**
	 * Sticks are normally played when a note block is on top of a glass
	 * block.
	 */
	STICKS(0x3),
	/**
	 * Bass guitar is normally played when a note block is on top of a wooden
	 * block.
	 */
	BASS_GUITAR(0x4);

	private static final ConcurrentMap<Byte, Instrument> BY_DATA = Concurrent.newMap();
	private final byte type;

	static {
		for (Instrument instrument : values())
			BY_DATA.put(instrument.getType(), instrument);
	}

	Instrument(int type) {
		this.type = (byte)type;
	}

	public byte getType() {
		return this.type;
	}

	public static Instrument getByType(byte type) {
		return BY_DATA.get(type);
	}

}