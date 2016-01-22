package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class PlayerPostLoginEvent extends PlayerEvent {

	public PlayerPostLoginEvent(BukkitMojangProfile profile) {
		super(profile);
	}

}