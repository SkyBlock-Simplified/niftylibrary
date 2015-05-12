package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftycore.mojang.MojangProfile;

public class BungeePlayerJoinEvent extends BungeePlayerEvent {

	public BungeePlayerJoinEvent(MojangProfile profile) {
		super(profile);
	}

}