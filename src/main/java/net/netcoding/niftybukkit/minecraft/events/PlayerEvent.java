package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftycore.mojang.MojangProfile;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class PlayerEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final MojangProfile profile;

	PlayerEvent(MojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public MojangProfile getProfile() {
		return this.profile;
	}

}
