package net.netcoding.nifty.common._new_.minecraft.material.types;

import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum SandstoneType {

	CRACKED(0x0),
	GLYPHED(0x1),
	SMOOTH(0x2);

	private static final ConcurrentMap<Byte, SandstoneType> BY_DATA = new ConcurrentMap<>();
	private final byte data;

	static {
		for (SandstoneType type : values())
			BY_DATA.put(type.data, type);
	}

	SandstoneType(int data) {
		this.data = (byte)data;
	}

	public byte getData() {
		return this.data;
	}

	public static SandstoneType getByData(final byte data) {
		return BY_DATA.get(data);
	}

}