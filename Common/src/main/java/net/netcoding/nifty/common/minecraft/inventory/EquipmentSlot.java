package net.netcoding.nifty.common.minecraft.inventory;

import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Arrays;

public enum EquipmentSlot {

	/**
	 * ALL is used for Attributes, don't attempt to use it elsewhere.
	 */
	ALL(""),
	HAND("mainhand"),
	OFF_HAND("offhand"),
	FEET("feet"),
	LEGS("legs"),
	CHEST("chest"),
	HEAD("head");

	private static final ConcurrentMap<String, EquipmentSlot> BY_KEY = Concurrent.newMap();
	private final String key;

	static {
		Arrays.stream(values()).forEach(slot -> BY_KEY.put(slot.getKey(), slot));
	}

	EquipmentSlot(String key) {
		this.key = key;
	}

	public static EquipmentSlot getByKey(String key) {
		return BY_KEY.containsKey(key) ? BY_KEY.get(key) : ALL;
	}

	public String getKey() {
		return this.key;
	}

}