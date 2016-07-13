package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public class BungeeProfileJoinEvent extends BungeeProfileEvent {

	public BungeeProfileJoinEvent(MinecraftMojangProfile profile) {
		super(profile);
	}

}