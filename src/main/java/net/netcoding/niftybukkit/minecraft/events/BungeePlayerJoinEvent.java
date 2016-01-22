package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class BungeePlayerJoinEvent extends BungeePlayerEvent {

	public BungeePlayerJoinEvent(BukkitMojangProfile profile) {
		super(profile);
	}

}