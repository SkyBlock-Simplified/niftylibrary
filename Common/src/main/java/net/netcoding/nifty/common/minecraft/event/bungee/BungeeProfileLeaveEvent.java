package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public class BungeeProfileLeaveEvent extends BungeeProfileEvent {

	public BungeeProfileLeaveEvent(MinecraftMojangProfile profile) {
		super(profile);
	}

}