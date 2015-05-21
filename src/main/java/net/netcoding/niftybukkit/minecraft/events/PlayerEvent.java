package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class PlayerEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final BukkitMojangProfile profile;

	PlayerEvent(BukkitMojangProfile profile) {
		this.profile = profile;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

}
