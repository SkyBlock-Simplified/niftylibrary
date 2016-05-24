package net.netcoding.niftybukkit.minecraft.events.bungee;

import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

class BungeeProfileEvent extends Event {

	private static final transient HandlerList handlers = new HandlerList();
	private final BukkitMojangProfile profile;

	BungeeProfileEvent(BukkitMojangProfile profile) {
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

	public BungeeServer getServer() {
		return this.getProfile().getServer();
	}

}
