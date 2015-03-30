package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.MojangProfile;

public class PlayerPostLoginEvent extends PlayerEvent {

	public PlayerPostLoginEvent(MojangProfile profile) {
		super(profile);
	}

}