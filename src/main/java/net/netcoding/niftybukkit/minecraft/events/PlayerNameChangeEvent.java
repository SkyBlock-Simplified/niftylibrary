package net.netcoding.niftybukkit.minecraft.events;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

public class PlayerNameChangeEvent extends PlayerEvent {

	public PlayerNameChangeEvent(BukkitMojangProfile profile) {
		super(profile);
	}

}