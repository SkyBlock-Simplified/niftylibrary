package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

public class BungeePlayerJoinEvent extends BungeePlayerEvent {

	public BungeePlayerJoinEvent(MojangProfile profile) {
		super(profile);
	}

}