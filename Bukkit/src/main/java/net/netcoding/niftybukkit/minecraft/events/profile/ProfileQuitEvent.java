package net.netcoding.niftybukkit.minecraft.events.profile;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileQuitEvent extends ProfileEvent {

	private final PlayerQuitEvent event;

	public ProfileQuitEvent(PlayerQuitEvent event, BukkitMojangProfile profile) {
		super(profile);
		this.event = event;
	}

	public String getQuitMessage(String message) {
		return this.event.getQuitMessage();
	}

	public void setQuitMessage(String message) {
		this.event.setQuitMessage(message);
	}

}