package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPostLoginEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final MojangProfile profile;

	public PlayerPostLoginEvent(MojangProfile profile) {
		this.profile = profile;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Deprecated
	public Player getPlayer() {
		return this.getProfile().getOfflinePlayer().getPlayer();
	}

	public MojangProfile getProfile() {
		return this.profile;
	}

}