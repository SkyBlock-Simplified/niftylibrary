package net.netcoding.nifty.common.minecraft.event.bungee;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

import java.util.Set;

public final class BungeeLoadedEvent<T extends MinecraftMojangProfile> implements BungeeEvent {

	public Set<BungeeServer<MinecraftMojangProfile>> getServers() {
		return Nifty.getBungeeHelper().getServers();
	}

}