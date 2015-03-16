package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDisconnectEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final MojangProfile profile;
	private final boolean kicked;

	public PlayerDisconnectEvent(MojangProfile profile, boolean kicked) {
		this.profile = profile;
		this.kicked = kicked;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public MojangProfile getProfile() {
		return this.profile;
	}

	public boolean isKicked() {
		return this.kicked;
	}

}