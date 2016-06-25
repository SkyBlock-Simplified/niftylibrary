package net.netcoding.nifty.common._new_.mojang;

import net.netcoding.nifty.common._new_.api.Event;
import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerJoinEvent;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.BukkitListener;
import net.netcoding.nifty.core.mojang.MojangRepository;

import java.util.UUID;

public abstract class BukkitMojangRepository<T extends BukkitMojangProfile> extends MojangRepository<T, Player> {

	static {
		new RepositoryListener();
	}

	@Override
	protected final boolean isOnline() {
		return Nifty.getServer().isOnlineMode() || Nifty.getBungeeHelper().getDetails().isDetected();
	}

	private static class RepositoryListener extends BukkitListener {

		public RepositoryListener() {
			super(Nifty.getPlugin());
		}

		@Event
		public void onProfileJoin(PlayerJoinEvent event) {
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