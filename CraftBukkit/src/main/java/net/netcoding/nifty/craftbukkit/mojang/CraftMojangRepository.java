package net.netcoding.nifty.craftbukkit.mojang;

import com.google.gson.JsonObject;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeServer;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common.mojang.BukkitMojangRepository;
import net.netcoding.nifty.core.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.craftbukkit.NiftyCraftBukkit;
import net.netcoding.nifty.craftbukkit.api.plugin.messaging.CraftBungeeHelper;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftPlayer;
import org.bukkit.OfflinePlayer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CraftMojangRepository extends BukkitMojangRepository<CraftMojangProfile> {

	private static CraftMojangRepository INSTANCE;

	private CraftMojangRepository() { }

	public static CraftMojangRepository getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CraftMojangRepository();

		return INSTANCE;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected final void processOfflineUsernames(List<CraftMojangProfile> profiles, ConcurrentList<String> userList) {
		for (String name : userList) {
			OfflinePlayer player = NiftyCraftBukkit.getPlugin(NiftyCraftBukkit.class).getServer().getOfflinePlayer(name);
			UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", player.getName()).getBytes(StandardCharsets.UTF_8));
			boolean isOfflineId = player.getUniqueId().equals(offlineId);
			boolean useOfflineId = !this.isOnline() || !isOfflineId;

			if (useOfflineId) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				CraftMojangProfile profile = GSON.fromJson(json.toString(), CraftMojangProfile.class);
				profiles.add(profile);
				userList.remove(name);
			} else if (!this.isOnline())
				userList.remove(name);
		}
	}

	@Override
	protected final CraftMojangProfile processOfflineUniqueId(UUID uniqueId) {
		OfflinePlayer player = NiftyCraftBukkit.getPlugin(NiftyCraftBukkit.class).getServer().getOfflinePlayer(uniqueId);
		UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", player.getName()).getBytes(StandardCharsets.UTF_8));
		boolean isOfflineId = player.getUniqueId().equals(offlineId);
		boolean useOfflineId = !this.isOnline() || !isOfflineId;
		CraftMojangProfile profile = null;

		if (StringUtil.notEmpty(player.getName()) && useOfflineId) {
			JsonObject json = new JsonObject();
			json.addProperty("id", player.getUniqueId().toString());
			json.addProperty("name", player.getName());
			profile = GSON.fromJson(json.toString(), CraftMojangProfile.class);
		}

		return profile;
	}

	@Override
	protected void processOnlineUsernames(List<CraftMojangProfile> profiles, ConcurrentList<String> userList) {
		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			for (String name : userList) {
				String criteriaName = name.toLowerCase();

				for (BungeeServer<CraftMojangProfile> server : CraftBungeeHelper.getInstance().getServers()) {
					CraftMojangProfile found = null;

					for (CraftMojangProfile profile : server.getPlayerList()) {
						if (profile.getName().equalsIgnoreCase(criteriaName)) {
							found = profile;
							break;
						}
					}

					if (found == null) {
						for (CraftMojangProfile profile : server.getPlayerList()) {
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
	protected CraftMojangProfile processOnlineUniqueId(UUID uniqueId) {
		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			for (BungeeServer<CraftMojangProfile> server : CraftBungeeHelper.getInstance().getServers()) {
				for (CraftMojangProfile profile : server.getPlayerList()) {
					if (profile.getUniqueId().equals(uniqueId))
						return profile;
				}
			}
		}

		return null;
	}

	/**
	 * Locates the profile for this server associated with the given bukkit player.
	 *
	 * @param player Bukkit player to search with.
	 * @return Profile associated with the given player.
	 * @throws ProfileNotFoundException If unable to locate the players profile.
	 */
	public CraftMojangProfile searchByBukkitPlayer(org.bukkit.entity.Player player) throws ProfileNotFoundException {
		try {
			return this.searchByBukkitPlayer(Collections.singletonList(player))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER == pnfex.getReason()) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, GSON.fromJson(json, BukkitMojangProfile.class));
			}

			throw pnfex;
		}
	}

	/**
	 * Locates the profile for this server associated with the given player.
	 *
	 * @param player Player to search with.
	 * @return Profile associated with the given player.
	 * @throws ProfileNotFoundException If unable to locate the players profile.
	 */
	@Override
	public CraftMojangProfile searchByPlayer(Player player) throws ProfileNotFoundException {
		try {
			return this.searchByPlayer(Collections.singletonList(player))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER == pnfex.getReason()) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, GSON.fromJson(json, BukkitMojangProfile.class));
			}

			throw pnfex;
		}
	}

	/**
	 * Locates the profiles for this server associated with the given bukkit players.
	 *
	 * @param players Bukkit players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	public CraftMojangProfile[] searchByBukkitPlayer(org.bukkit.entity.Player[] players) throws ProfileNotFoundException {
		return this.searchByBukkitPlayer(Arrays.asList(players));
	}

	/**
	 * Locates the profiles for this server associated with the given players.
	 *
	 * @param players Players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	@Override
	public CraftMojangProfile[] searchByPlayer(Player[] players) throws ProfileNotFoundException {
		return this.searchByPlayer(Arrays.asList(players));
	}

	/**
	 * Locates the profiles for this server associated with the given bukkit players.
	 *
	 * @param players Bukkit players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	public CraftMojangProfile[] searchByBukkitPlayer(Collection<? extends org.bukkit.entity.Player> players) throws ProfileNotFoundException {
		return this.searchByPlayer(players.stream().map((Function<org.bukkit.entity.Player, CraftPlayer>) CraftPlayer::new).collect(Collectors.toList()));
	}

	/**
	 * Locates the profiles for this server associated with the given players.
	 *
	 * @param players Players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	@Override
	public CraftMojangProfile[] searchByPlayer(Collection<? extends Player> players) throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.OFFLINE_PLAYERS;
		List<CraftMojangProfile> profiles = new ArrayList<>();
		ConcurrentList<CraftMojangProfile> tempProfiles = new ConcurrentList<>();

		try {
			// Create Temporary Matching Profiles
			for (Player player : players) {
				JsonObject json = new JsonObject();
				json.addProperty("id", player.getUniqueId().toString());
				json.addProperty("name", player.getName());
				tempProfiles.add(GSON.fromJson(json, CraftMojangProfile.class));
			}

			// Search Online Servers
			if (Nifty.getBungeeHelper().getDetails().isDetected()) {
				for (BukkitMojangProfile temp : tempProfiles) {
					// TODO: BungeeHelper
					for (CraftMojangProfile profile : CraftBungeeHelper.getInstance().getServer().getPlayerList()) {
						if (profile.equals(temp)) {
							profiles.add(profile);
							tempProfiles.remove(temp);
							break;
						}
					}
				}
			}

			// Search Unique ID
			for (CraftMojangProfile temp : tempProfiles) {
				CraftMojangProfile profile = this.searchByUniqueId(temp.getUniqueId());
				profiles.add(profile);
				tempProfiles.remove(temp);
			}

			return ListUtil.toArray(profiles, CraftMojangProfile.class);
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), type, pnfex.getCause(), ListUtil.toArray(tempProfiles, BukkitMojangProfile.class));
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(tempProfiles, BukkitMojangProfile.class));
		}
	}

}