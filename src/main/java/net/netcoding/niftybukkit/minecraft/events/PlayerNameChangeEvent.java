package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftycore.mojang.MojangProfile;

public class PlayerNameChangeEvent extends PlayerEvent {

	public PlayerNameChangeEvent(MojangProfile profile) {
		super(profile);
	}

}