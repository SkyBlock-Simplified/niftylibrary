package net.netcoding.niftybukkit.minecraft.events.bungee;

import net.netcoding.niftybukkit.minecraft.events.profile.ProfileEvent;
import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

abstract class BungeeProfileEvent extends ProfileEvent {

	BungeeProfileEvent(BukkitMojangProfile profile) {
		super(profile);
	}

	public BungeeServer getServer() {
		return this.getProfile().getServer();
	}

}
