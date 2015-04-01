package net.netcoding.niftybukkit.mojang;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.http.HttpStatus;
import net.netcoding.niftybukkit.http.exceptions.HttpConnectionException;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;
import net.netcoding.niftybukkit.util.gson.Gson;
import net.netcoding.niftybukkit.util.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;

/**
 * A collection of methods to locate player UUID and Name throughout Bungee or offline.
 */
public class MojangRepository {

	// API: http://wiki.vg/Mojang_API
	private static final int PROFILES_PER_REQUEST = 100;
	private static long LAST_HTTP_REQUEST = System.currentTimeMillis();
	private static final transient Gson GSON = new Gson();
	private static final transient HttpClient HTTP = new HttpClient();
	private static final transient ConcurrentSet<MojangProfile> CACHE = new ConcurrentSet<>();

	static {
		new RepositoryListener();
	}

	private static URL getProfilesUrl() throws MalformedURLException {
		return new URL("https://api.mojang.com/profiles/minecraft");
	}

	private static URL getProfilesUrl(String username) throws MalformedURLException {
		return getProfilesUrl(username, true);
	}

	private static URL getProfilesUrl(String username, boolean useAt) throws MalformedURLException {
		return new URL(StringUtil.format("https://api.mojang.com/users/profiles/minecraft/{0}{1}", username, (useAt ? "?at=0" : "")));
	}

	private static URL getNamesUrl(UUID uniqueId) throws MalformedURLException {
		return new URL(StringUtil.format("https://api.mojang.com/user/profiles/{0}/names", uniqueId.toString().replace("-", "")));
	}

	/**
	 * Locates the profile for this server associated with the given offline player.
	 * 
	 * @param oplayer Offline player to search with.
	 * @return Profile associated with the given player.
	 * @throws ProfileNotFoundException If unable to locate the players profile.
	 */
	public MojangProfile searchByPlayer(OfflinePlayer oplayer) throws ProfileNotFoundException {
		try {
			return searchByPlayer(Arrays.asList(oplayer))[0];
		} catch (ProfileNotFoundException pnfex) {
			if (ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER.equals(pnfex.getReason()))
				throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.OFFLINE_PLAYER, oplayer);

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
	public MojangProfile[] searchByPlayer(OfflinePlayer... oplayers) throws ProfileNotFoundException {
		return searchByPlayer(Arrays.asList(oplayers));
	}

	/**
	 * Locates the profiles for this server associated with the given offline players.
	 * 
	 * @param oplayers Offline players to search with.
	 * @return Profiles associated with the list of players.
	 * @throws ProfileNotFoundException If unable to locate any players profile.
	 */
	public MojangProfile[] searchByPlayer(Collection<? extends OfflinePlayer> oplayers) throws ProfileNotFoundException {
		List<MojangProfile> profiles = new ArrayList<>();

		try {
			ConcurrentList<OfflinePlayer> foplayers = new ConcurrentList<>(oplayers);

			// Search Online Servers
			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				for (OfflinePlayer oplayer : foplayers) {
					for (MojangProfile profile : NiftyBukkit.getBungeeHelper().getServer().getPlayerList()) {
						if (profile.belongsTo(oplayer)) {
							profiles.add(profile);
							foplayers.remove(oplayer);
							break;
						}
					}
				}
			}

			// Search Unique ID
			for (OfflinePlayer oplayer : foplayers) {
				MojangProfile profile = this.searchByUniqueId(oplayer.getUniqueId());
				profiles.add(profile);
				foplayers.remove(oplayer);
			}

			return ListUtil.toArray(profiles, MojangProfile.class);
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), ProfileNotFoundException.LookupType.OFFLINE_PLAYERS, pnfex.getCause(), ListUtil.toArray(oplayers, OfflinePlayer.class));
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, ProfileNotFoundException.LookupType.USERNAMES, ex, ListUtil.toArray(oplayers, OfflinePlayer.class));
		}
	}

	/**
	 * Locates the profile associated with the given username.
	 * 
	 * @param username Username to search with.
	 * @return Profile associated with the given username.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	public MojangProfile searchByUsername(String username) throws ProfileNotFoundException {
		try {
			return searchByUsername(Arrays.asList(username))[0];
		} catch (ProfileNotFoundException pnfex) {
			throw new ProfileNotFoundException(pnfex.getReason(), ProfileNotFoundException.LookupType.USERNAME, pnfex.getCause(), username);
		}
	}

	/**
	 * Locates the profiles associated with the given usernames.
	 * 
	 * @param usernames Usernames to search with.
	 * @param useApi    Use the Mojang API in the search.
	 * @return Profiles associated with the given usernames.
	 * @throws ProfileNotFoundException If unable to locate any users profile.
	 */
	public MojangProfile[] searchByUsername(Collection<String> usernames) throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.USERNAMES;
		List<MojangProfile> profiles = new ArrayList<>();

		try {
			ConcurrentList<String> userList = new ConcurrentList<>(usernames);

			// Remove Expired Cache Profiles
			for (MojangProfile profile : CACHE) {
				if (profile.hasExpired()) {
					CACHE.remove(profile);
					continue;
				}
			}

			// Check Cache Profiles
			if (CACHE.size() > 0) {
				for (MojangProfile profile : CACHE) {
					for (String name : userList) {
						if (profile.getName().equalsIgnoreCase(name)) {
							profiles.add(profile);
							userList.remove(name);
							break;
						}
					}
				}

				for (MojangProfile profile : CACHE) {
					for (String name : userList) {
						String criteriaName = name.toLowerCase();

						if (profile.getName().toLowerCase().startsWith(criteriaName.toLowerCase())) {
							profiles.add(profile);
							userList.remove(name);
							break;
						}
					}
				}
			}

			// Check Online Servers
			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				for (String name : userList) {
					String criteriaName = name.toLowerCase();

					for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
						if (server.isOnline()) {
							MojangProfile found = null;

							for (MojangProfile profile : server.getPlayerList()) {
								if (profile.getName().equalsIgnoreCase(criteriaName)) found = profile;
								if (found != null) break;
							}

							if (found == null) {
								for (MojangProfile profile : server.getPlayerList()) {
									if (profile.getName().toLowerCase().startsWith(criteriaName)) found = profile;
									if (found != null) break;
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

			// Check Offline Player Cache
			for (String name : userList) {
				OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(name);
				UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", oplayer.getName()).getBytes(StandardCharsets.UTF_8));
				boolean isOnline = Bukkit.getServer().getOnlineMode() || NiftyBukkit.getBungeeHelper().isOnlineMode();
				boolean isOfflineId = oplayer.getUniqueId().equals(offlineId);
				boolean useOfflineId = !isOnline || !isOfflineId;

				if (useOfflineId) {
					JsonObject json = new JsonObject();
					json.addProperty("id", oplayer.getUniqueId().toString());
					json.addProperty("name", oplayer.getName());
					profiles.add(GSON.fromJson(json.toString(), MojangProfile.class));
					userList.remove(name);
				} else if (!isOnline)
					userList.remove(name);
			}

			// Query Mojang API
			if (userList.size() > 0) {
				List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));
				String[] userArray = ListUtil.toArray(userList, String.class);
				int start = 0;
				int i = 0;

				do {
					int end = PROFILES_PER_REQUEST * (i + 1);
					if (end > userList.size()) end = userList.size();
					String[] batch = Arrays.copyOfRange(userArray, start, end);
					HttpBody body = new HttpBody(GSON.toJson(batch));
					long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();

					try {
						if (wait > 0) Thread.sleep(wait);
						MojangProfile[] result = GSON.fromJson(HTTP.post(getProfilesUrl(), body, headers), MojangProfile[].class);
						profiles.addAll(Arrays.asList(result));
						CACHE.addAll(Arrays.asList(result));
					} finally {
						LAST_HTTP_REQUEST = System.currentTimeMillis();
						start = end;
						i++;
					}
				} while (start < userList.size());

				for (MojangProfile profile : profiles)
					userList.remove(profile.getName());

				for (String user : userList) {
					long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();

					try {
						if (wait > 0) Thread.sleep(wait);
						MojangProfile result = GSON.fromJson(HTTP.get(getProfilesUrl(user)), MojangProfile.class);

						if (result != null) {
							profiles.add(result);
							CACHE.add(result);
						}
					} finally {
						LAST_HTTP_REQUEST = System.currentTimeMillis();
					}
				}

				for (MojangProfile profile : profiles)
					userList.remove(profile.getName());

				for (String user : userList) {
					long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();

					try {
						if (wait > 0) Thread.sleep(wait);
						MojangProfile result = GSON.fromJson(HTTP.get(getProfilesUrl(user, false)), MojangProfile.class);

						if (result != null) {
							profiles.add(result);
							CACHE.add(result);
						}
					} finally {
						LAST_HTTP_REQUEST = System.currentTimeMillis();
					}
				}
			}
		} catch (HttpConnectionException hcex) {
			ProfileNotFoundException.Reason reason = ProfileNotFoundException.Reason.EXCEPTION;

			if (hcex.getStatus().equals(HttpStatus.TOO_MANY_REQUESTS))
				reason = ProfileNotFoundException.Reason.RATE_LIMITED;

			throw new ProfileNotFoundException(reason, type, hcex, ListUtil.toArray(usernames, String.class));
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, ListUtil.toArray(usernames, String.class));
		}

		if (profiles.size() == 0)
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, ProfileNotFoundException.LookupType.USERNAMES, ListUtil.toArray(usernames, String.class));

		return ListUtil.toArray(profiles, MojangProfile.class);
	}

	/**
	 * Locates the profile associated with the given Unique ID.
	 * 
	 * @param uniqueId Unique ID to search with.
	 * @return Profile associated with the given Unique ID.
	 * @throws ProfileNotFoundException If unable to locate users profile.
	 */
	public MojangProfile searchByUniqueId(final UUID uniqueId) throws ProfileNotFoundException {
		final ProfileNotFoundException.LookupType type = ProfileNotFoundException.LookupType.UNIQUE_ID;
		MojangProfile found = null;

		try {
			// Remove Expired Cache Profiles
			for (MojangProfile profile : CACHE) {
				if (profile.hasExpired()) {
					CACHE.remove(profile);
					continue;
				}
			}

			// Check Cache Profiles
			for (MojangProfile profile : CACHE) {
				if (profile.getUniqueId().equals(uniqueId)) {
					found = profile;
					break;
				}
			}

			// Check Online Servers
			if (found == null) {
				if (NiftyBukkit.getBungeeHelper().isDetected()) {
					for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
						if (server.isOnline()) {
							for (MojangProfile profile : server.getPlayerList()) {
								if (profile.getUniqueId().equals(uniqueId)) {
									found = profile;
									break;
								}
							}

							if (found != null) break;
						}
					}
				}
			}

			// Check Offline Player Cache
			OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(uniqueId);
			UUID offlineId = UUID.nameUUIDFromBytes(StringUtil.format("OfflinePlayer:{0}", oplayer.getName()).getBytes(StandardCharsets.UTF_8));
			boolean isOnline = Bukkit.getServer().getOnlineMode() || NiftyBukkit.getBungeeHelper().isOnlineMode();
			boolean isOfflineId = oplayer.getUniqueId().equals(offlineId);
			boolean useOfflineId = !isOnline || !isOfflineId;

			if (found == null && StringUtil.notEmpty(oplayer.getName()) && useOfflineId) {
				JsonObject json = new JsonObject();
				json.addProperty("id", oplayer.getUniqueId().toString());
				json.addProperty("name", oplayer.getName());
				found = GSON.fromJson(json.toString(), MojangProfile.class);
			}

			// Query Mojang API
			if (found == null && !useOfflineId) {
				try {
					long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();
					if (wait > 0) Thread.sleep(wait);
					UUIDSearchResult[] results = GSON.fromJson(HTTP.get(getNamesUrl(uniqueId)), UUIDSearchResult[].class);

					if (results != null && results.length > 0) {
						UUIDSearchResult result = results[0];
						JsonObject json = new JsonObject();
						json.addProperty("id", uniqueId.toString());
						json.addProperty("name", result.getName());
						found = GSON.fromJson(json.toString(), MojangProfile.class);
						CACHE.add(found);
					}
				} finally {
					LAST_HTTP_REQUEST = System.currentTimeMillis();
				}
			}
		} catch (HttpConnectionException hcex) {
			ProfileNotFoundException.Reason reason = ProfileNotFoundException.Reason.EXCEPTION;

			if (hcex.getStatus().equals(HttpStatus.TOO_MANY_REQUESTS))
				reason = ProfileNotFoundException.Reason.RATE_LIMITED;

			throw new ProfileNotFoundException(reason, type, hcex, uniqueId);
		} catch (Exception ex) {
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.EXCEPTION, type, ex, uniqueId);
		}

		if (found == null)
			throw new ProfileNotFoundException(ProfileNotFoundException.Reason.NO_PREMIUM_PLAYER, type, uniqueId);

		return found;
	}

	@SuppressWarnings("unused")
	private static class UUIDSearchResult {

		private String name;
		private long changedToAt;

		public long getChangedToAt() {
			return this.changedToAt;
		}

		public String getName() {
			return this.name;
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