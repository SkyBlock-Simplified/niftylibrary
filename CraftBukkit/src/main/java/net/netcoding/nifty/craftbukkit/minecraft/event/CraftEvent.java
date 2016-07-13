package net.netcoding.nifty.craftbukkit.minecraft.event;

import net.netcoding.nifty.common.minecraft.event.Event;

public abstract class CraftEvent implements Event {

	private final org.bukkit.event.Event event;

	public CraftEvent(org.bukkit.event.Event event) {
		this.event = event;
	}

	public org.bukkit.event.Event getHandle() {
		return this.event;
	}

}