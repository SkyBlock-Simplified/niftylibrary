package net.netcoding.nifty.common.mojang;

import com.google.gson.JsonObject;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.Event;
import net.netcoding.nifty.common.api.plugin.MinecraftListener;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.event.player.PlayerJoinEvent;
import net.netcoding.nifty.core.mojang.MojangRepository;
import net.netcoding.nifty.core.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;

import java.util.*;

public abstract class MinecraftMojangRepository<T extends MinecraftMojangProfile> extends MojangRepository<T, Player> {

	protected MinecraftMojangRepository() {
		new RepositoryListener();
	}

	@Override
	protected final boolean isOnline() {
		return Nifty.getServer().isOnline() || Nifty.getBungeeHelper().getDetails().isDetected();
	}

	@Override
	protected final void processOfflineUsernames(List<T> profiles, ConcurrentList<String> userList) {
		for (String name : userList) {
			OfflinePlayer player = Nifty.getServer().getOfflinePlayer(name);
			UUID offlineId = OfflinePlayer.getOfflineUniqueId(player.getName());
			boolean isOfflineId = player.getUniqueId().equals(offlineId);
			boolean useOfflineId = !this.isOnline() || !isOfflineId;

			if (useOfflineId) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				T profile = GSON.fromJson(json.toString(), this.getSuperClass());
				profiles.add(profile);
				userList.remove(name);
			} else if (!this.isOnline())
				userList.remove(name);
		}
	}

	@Override
	protected final T processOfflineUniqueId(UUID uniqueId) {
		OfflinePlayer player = Nifty.getServer().getOfflinePlayer(uniqueId);
		UUID offlineId = OfflinePlayer.getOfflineUniqueId(player.getName());
		boolean isOfflineId = player.getUniqueId().equals(offlineId);
		boolean useOfflineId = !this.isOnline() || !isOfflineId;
		T profile = null;

		if (StringUtil.notEmpty(player.getName()) && useOfflineId) {
			JsonObject json = new JsonObject();
			json.addProperty("id", player.getUniqueId().toString());
			json.addProperty("name", player.getName());
			profile = GSON.fromJson(json.toString(), this.getSuperClass());
		}

		return profile;
	}

	@Override
	protected final void processOnlineUsernames(List<T> profiles, ConcurrentList<String> userList) {
		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			for (String name : userList) {
				String criteriaName = name.toLowerCase();

				for (BungeeServer<T> server : Nifty.<T>getBungeeHelper().getServers()) {
					T found = null;

					for (T profile : server.getPlayerList()) {
						if (profile.getName().equalsIgnoreCase(criteriaName)) {
							found = profile;
							break;
						}
					}

					if (found == null) {
						for (T profile : server.getPlayerList()) {
							if (profile.getName().toLowerCase().startsWith(criteriaName)) {
								found = profile;
								break;
							}
						}
					}

					if (found != null) {
						profiles.add(found);
						userList.remove(name);
						break;
					}
				}
			}
		}
	}

	@Override
	protected final T processOnlineUniqueId(UUID uniqueId) {
		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			for (BungeeServer<T> server : Nifty.<T>getBungeeHelper().getServers()) {
				for (T profile : server.getPlayerList()) {
					if (profile.getUniqueId().equals(uniqueId))
						return profile;
				}
			}
		}

		return null;
	}

	@Override
	public final T searchByPlayer(Player player) throws ProfileNotFoundException {
		try {
			return this.searchByPlayer(Collections.singletonList(player))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER == pnfex.getReason()) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, GSON.fromJson(json, this.getSuperClass()));
			}

			throw pnfex;
		}
	}

	@Override
	public final T[] searchByPlayer(Player[] players) throws ProfileNotFoundException {
		return this.searchByPlayer(Arrays.asList(players));
	}

	@Override
	public final T[] searchByPlayer(Collection<? extends Player> players) throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.OFFLINE_PLAYERS;
		ConcurrentList<T> profiles = Concurrent.newList();
		ConcurrentList<T> tempProfiles = Concurrent.newList();

		try {
			// Create Temporary Matching Profiles
			for (Player player : players) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				tempProfiles.add(GSON.fromJson(json, this.getSuperClass()));
			}

			// Search Online Servers
			if (Nifty.getBungeeHelper().getDetails().isDetected()) {
				for (T temp : tempProfiles) {
					for (T profile : Nifty.<T>getBungeeHelper().getServer().getPlayerList()) {
						if (profile.equals(temp)) {
							profiles.add(profile);
							tempProfiles.remove(temp);
							break;
						}
					}
				}
			}

			// Search Unique ID
			for (T temp : tempProfiles) {
				T profile = this.searchByUniqueId(temp.getUniqueId());
				profiles.add(profile);
				tempProfiles.remove(temp);
			}

			return ListUtil.toArray(profiles, this.getSuperClass());
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), type, pnfex.getCause(), ListUtil.toArray(tempProfiles, this.getSuperClass()));
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(tempProfiles, this.getSuperClass()));
		}
	}

	private class RepositoryListener extends MinecraftListener {

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
					cache.stream().filter(cache -> cache.equals(profile)).forEach(cache::remove));
			}
		}

	}

}