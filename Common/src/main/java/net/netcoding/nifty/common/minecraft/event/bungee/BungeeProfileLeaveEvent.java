package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public class BungeeProfileLeaveEvent extends BungeeProfileEvent {

	public BungeeProfileLeaveEvent(BukkitMojangProfile profile) {
		super(profile);
	}

}