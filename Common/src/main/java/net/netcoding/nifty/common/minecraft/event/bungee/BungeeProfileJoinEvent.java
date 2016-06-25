package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public class BungeeProfileJoinEvent extends BungeeProfileEvent {

	public BungeeProfileJoinEvent(BukkitMojangProfile profile) {
		super(profile);
	}

}