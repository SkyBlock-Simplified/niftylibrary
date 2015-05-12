package net.netcoding.niftybukkit.mojang;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.mojang.MojangRepository;
import net.netcoding.niftycore.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import net.netcoding.niftycore.util.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;

public class BukkitMojangRepository extends MojangRepository<BukkitMojangProfile> {

	static {
		new RepositoryListener();
	}

	@Override
	protected final boolean isOnline() {
		return Bukkit.getServer().getOnlineMode() || NiftyBukkit.getBungeeHelper().isOnlineMode();
	}

	@Override
	protected final void processOfflineUsernames(List<BukkitMojangProfile> profiles, ConcurrentList<String> userList) {
		for (String name : userList) {
			OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(name);
			UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", oplayer.getName()).getBytes(StandardCharsets.UTF_8));
			boolean isOfflineId = oplayer.getUniqueId().equals(offlineId);
			boolean useOfflineId = !this.isOnline() || !isOfflineId;

			if (useOfflineId) {
				JsonObject json = new JsonObject();
				json.addProperty("id", oplayer.getUniqueId().toString());
				json.addProperty("name", oplayer.getName());
				profiles.add(GSON.fromJson(json.toString(), BukkitMojangProfile.class));
				userList.remove(name);
			} else if (!this.isOnline())
				userList.remove(name);
		}
	}

	@Override
	protected final void processOnlineUsernames(List<BukkitMojangProfile> profiles, ConcurrentList<String> userList) {
		if (NiftyBukkit.getBungeeHelper().isDetected()) {
			for (String name : userList) {
				String criteriaName = name.toLowerCase();

				for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
					MojangProfile found = null;

					for (MojangProfile profile : server.getPlayerList()) {
						if (profile.getName().equalsIgnoreCase(criteriaName)) {
							found = profile;
							break;
						}
					}

					if (found == null) {
						for (MojangProfile profile : server.getPlayerList()) {
							if (profile.getName().toLowerCase().startsWith(criteriaName)) {
								found = profile;
								break;
							}
						}
					}

					if (found != null) {
						profiles.add((BukkitMojangProfile)found);
						userList.remove(name);
						break;
					}
				}
			}
		}
	}

	@Override
	protected final BukkitMojangProfile processOfflineUniqueId(UUID uniqueId) {
		OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(uniqueId);
		UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", oplayer.getName()).getBytes(StandardCharsets.UTF_8));
		boolean isOfflineId = oplayer.getUniqueId().equals(offlineId);
		boolean useOfflineId = !this.isOnline() || !isOfflineId;
		BukkitMojangProfile bukkitProfile = null;

		if (StringUtil.notEmpty(oplayer.getName()) && useOfflineId) {
			JsonObject json = new JsonObject();
			json.addProperty("id", oplayer.getUniqueId().toString());
			json.addProperty("name", oplayer.getName());
			bukkitProfile = GSON.fromJson(json.toString(), BukkitMojangProfile.class);
		}

		return bukkitProfile;
	}

	@Override
	protected final BukkitMojangProfile processOnlineUniqueId(UUID uniqueId) {
		if (NiftyBukkit.getBungeeHelper().isDetected()) {
			for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
				for (MojangProfile profile : server.getPlayerList()) {
					if (profile.getUniqueId().equals(uniqueId))
						return (BukkitMojangProfile)profile;
				}
			}
		}

		return null;
	}

	/**
	 * Locates the profile for this server associated with the given offline player.
	 * 
	 * @param oplayer Offline player to search with.
	 * @return Profile associated with the given player.
	 * @throws ProfileNotFoundException If unable to locate the players profile.
	 */
	public BukkitMojangProfile searchByPlayer(OfflinePlayer oplayer) throws ProfileNotFoundException {
		try {
			return this.searchByPlayer(Arrays.asList(oplayer))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER.equals(pnfex.getReason())) {
				JsonObject json = new JsonObject();
				json.addProperty("id", oplayer.getUniqueId().toString());
				json.addProperty("name", oplayer.getName());
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, GSON.fromJson(json, BukkitMojangProfile.class));
			}

			throw pnfex;
		}
	}

	/**
	 * Locates the profiles for this server associated with the given offline players.
	 * 
	 * @param oplayers Offline players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	public BukkitMojangProfile[] searchByPlayer(OfflinePlayer[] oplayers) throws ProfileNotFoundException {
		return this.searchByPlayer(Arrays.asList(oplayers));
	}

	/**
	 * Locates the profiles for this server associated with the given offline players.
	 * 
	 * @param oplayers Offline players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	public BukkitMojangProfile[] searchByPlayer(Collection<? extends OfflinePlayer> oplayers) throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.OFFLINE_PLAYERS;
		List<BukkitMojangProfile> profiles = new ArrayList<>();
		ConcurrentList<BukkitMojangProfile> tempProfiles = new ConcurrentList<>();

		try {
			// Create Temporary Matching Profiles
			for (OfflinePlayer oplayer : oplayers) {
				JsonObject json = new JsonObject();
				json.addProperty("id", oplayer.getUniqueId().toString());
				json.addProperty("name", oplayer.getName());
				tempProfiles.add(GSON.fromJson(json, BukkitMojangProfile.class));
			}

			// Search Online Servers
			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				for (MojangProfile temp : tempProfiles) {
					for (MojangProfile profile : NiftyBukkit.getBungeeHelper().getServer().getPlayerList()) {
						if (profile.equals(temp)) {
							profiles.add((BukkitMojangProfile)profile);
							tempProfiles.remove(temp);
							break;
						}
					}
				}
			}

			// Search Unique ID
			for (MojangProfile temp : tempProfiles) {
				MojangProfile profile = this.searchByUniqueId(temp.getUniqueId());
				profiles.add((BukkitMojangProfile)profile);
				tempProfiles.remove(temp);
			}

			return ListUtil.toArray(profiles, BukkitMojangProfile.class);
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), type, pnfex.getCause(), ListUtil.toArray(tempProfiles, BukkitMojangProfile.class));
		} catch (Exception ex) {
			// TODO: Offline-Mode NullPointerException
			if (ex instanceof NullPointerException)
				ex.printStackTrace();

			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(tempProfiles, BukkitMojangProfile.class));
		}
	}

	private static class RepositoryListener extends BukkitListener {

		public RepositoryListener() {
			super(NiftyBukkit.getPlugin());
		}

		@EventHandler
		public void onPlayerPostLogin(final PlayerPostLoginEvent event) {
			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				UUID uuid = event.getProfile().getUniqueId();
				String name = event.getProfile().getName();

				for (MojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList()) {
					if (profile.getUniqueId().equals(uuid) || profile.getName().equalsIgnoreCase(name)) {
						for (MojangProfile cache : CACHE) {
							if (cache.equals(profile))
								CACHE.remove(cache);
						}
					}
				}
			}
		}
		
	}

}