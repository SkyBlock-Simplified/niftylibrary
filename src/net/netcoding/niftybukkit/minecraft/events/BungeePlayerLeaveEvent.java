package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

public class BungeePlayerLeaveEvent extends BungeePlayerEvent {

	public BungeePlayerLeaveEvent(MojangProfile profile) {
		super(profile);
	}

}