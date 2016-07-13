package net.netcoding.nifty.common.mojang;

import net.netcoding.nifty.common.api.plugin.Event;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.player.PlayerJoinEvent;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.MinecraftListener;
import net.netcoding.nifty.core.mojang.MojangRepository;

import java.util.UUID;

public abstract class MinecraftMojangRepository<T extends MinecraftMojangProfile> extends MojangRepository<T, Player> {

	static {
		new RepositoryListener();
	}

	@Override
	protected final boolean isOnline() {
		return Nifty.getServer().isOnline() || Nifty.getBungeeHelper().getDetails().isDetected();
	}

	private static class RepositoryListener extends MinecraftListener {

		public RepositoryListener() {
			super(Nifty.getPlugin());
		}

		@Event
		public void onPlayerJoin(PlayerJoinEvent event) {
			if (Nifty.getBungeeHelper().getDetails().isDetected()) {
				UUID uuid = event.getProfile().getUniqueId();
				String name = event.getProfile().getName();

				Nifty.getBungeeHelper().getPlayerList().stream().filter(profile ->
					profile.getUniqueId().equals(uuid) || profile.getName().equalsIgnoreCase(name)).forEach(profile ->
					CACHE.stream().filter(cache -> cache.equals(profile)).forEach(CACHE::remove));
			}
		}

	}

}