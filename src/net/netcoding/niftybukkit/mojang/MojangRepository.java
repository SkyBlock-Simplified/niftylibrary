package net.netcoding.niftybukkit.mojang;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.event.EventHandler;

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
		return new URL(StringUtil.format("https://api.mojang.com/users/profiles/minecraft/{0}?at=0", username));
	}

	@Deprecated
	public MojangProfile searchByExactPlayer(OfflinePlayer player) throws ProfileNotFoundException {
		return searchByPlayer(player);
	}

	public MojangProfile searchByPlayer(OfflinePlayer player) throws ProfileNotFoundException {
		try {
			return searchByPlayer(Arrays.asList(player))[0];
		} catch (ProfileNotFoundException pnfe) {
			throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.OFFLINE_PLAYER, player);
		}
	}

	public MojangProfile[] searchByPlayer(OfflinePlayer... players) throws ProfileNotFoundException {
		return searchByPlayer(Arrays.asList(players));
	}

	public MojangProfile[] searchByPlayer(List<OfflinePlayer> players) throws ProfileNotFoundException {
		if (ListUtil.isEmpty(players)) throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.NULL, players);
		List<MojangProfile> profiles = new ArrayList<>();
		ConcurrentList<OfflinePlayer> oplayers = new ConcurrentList<>(players);

		if (NiftyBukkit.getBungeeHelper().isOnline()) {
			for (OfflinePlayer oplayer : oplayers) {
				for (MojangProfile profile : NiftyBukkit.getBungeeHelper().getServer().getPlayerList()) {
					if (profile.belongsTo(oplayer)) {
						profiles.add(profile);
						oplayers.remove(oplayer);
						break;
					}
				}

			}
		} else {
			for (OfflinePlayer oplayer : oplayers) {
				try {
					MojangProfile profile = this.searchByExactUUID(oplayer.getUniqueId());
					profiles.add(profile);
					oplayers.remove(oplayer);
				} catch (ProfileNotFoundException pnfe) { }
			}
		}

		if (profiles.size() == 0)
			throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.OFFLINE_PLAYERS, ListUtil.toArray(players, OfflinePlayer.class));
		else
			return ListUtil.toArray(profiles, MojangProfile.class);
	}

	@Deprecated
	public MojangProfile searchByExactUsername(String username) throws ProfileNotFoundException {
		return searchByUsername(username);
	}

	public MojangProfile searchByUsername(String username) throws ProfileNotFoundException {
		try {
			return searchByUsername(Arrays.asList(username))[0];
		} catch (ProfileNotFoundException pnfe) {
			throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.USERNAME, username);
		}
	}

	public MojangProfile[] searchByUsername(String... usernames) throws ProfileNotFoundException {
		return searchByUsername(Arrays.asList(usernames));
	}

	public MojangProfile[] searchByUsername(List<String> usernames) throws ProfileNotFoundException {
		if (ListUtil.isEmpty(usernames)) throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.NULL, usernames);
		List<MojangProfile> profiles = new ArrayList<>();
		ConcurrentList<String> userList = new ConcurrentList<>(usernames);

		if (CACHE.size() > 0) {
			for (String name : userList) {
				for (MojangProfile profile : CACHE) {
					if (profile.hasExpired()) {
						CACHE.remove(profile);
						continue;
					}
				}

				for (MojangProfile profile : CACHE) {
					if (profile.getName().equalsIgnoreCase(name)) {
						profiles.add(profile);
						userList.remove(name);
						break;
					}
				}

				for (MojangProfile profile : CACHE) {
					if (profile.getName().toLowerCase().startsWith(name)) {
						profiles.add(profile);
						userList.remove(name);
						break;
					}
				}
			}
		}

		if (userList.size() > 0) {
			if (NiftyBukkit.getBungeeHelper().isOnline()) {
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
			} else {
				for (String name : userList) {
					@SuppressWarnings("deprecation")
					OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(name);

					if (oplayer != null) {
						JsonObject json = new JsonObject();
						json.addProperty("id", oplayer.getUniqueId().toString());
						json.addProperty("name", oplayer.getName());
						profiles.add(GSON.fromJson(json.toString(), MojangProfile.class));
						userList.remove(name);
					}
				}
			}
		}

		if (userList.size() > 0) {
			try {
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
					if (wait > 0) Thread.sleep(wait);
					String response = HTTP.post(getProfilesUrl(), body, headers);
					LAST_HTTP_REQUEST = System.currentTimeMillis();
					MojangProfile[] result = GSON.fromJson(response, MojangProfile[].class);
					profiles.addAll(Arrays.asList(result));
					CACHE.addAll(Arrays.asList(result));
					start = end;
					i++;
				} while (start < userList.size());

				for (MojangProfile profile : profiles)
					userList.remove(profile.getName());

				for (String user : userList) {
					long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();
					if (wait > 0) Thread.sleep(wait);
					String response = HTTP.get(getProfilesUrl(user));
					LAST_HTTP_REQUEST = System.currentTimeMillis();
					MojangProfile result = GSON.fromJson(response, MojangProfile.class);

					if (result != null) {
						profiles.add(result);
						CACHE.add(result);
					}
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		if (profiles.size() == 0)
			throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.USERNAMES, ListUtil.toArray(usernames, String.class));
		else
			return ListUtil.toArray(profiles, MojangProfile.class);
	}

	public MojangProfile searchByExactUUID(final UUID uuid) throws ProfileNotFoundException {
		if (uuid == null) throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.NULL, uuid);
		MojangProfile found = null;

		if (CACHE.size() > 0) {
			for (MojangProfile profile : CACHE) {
				if (profile.hasExpired()) {
					CACHE.remove(profile);
					continue;
				}

				if (profile.getUniqueId().equals(uuid)) {
					found = profile;
					break;
				}
			}
		}

		if (found == null) {
			if (NiftyBukkit.getBungeeHelper().isOnline()) {
				for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
					if (server.isOnline()) {
						for (MojangProfile profile : server.getPlayerList()) {
							if (profile.getUniqueId().equals(uuid)) {
								found = profile;
								break;
							}
						}

						if (found != null) break;
					}
				}
			} else {
				OfflinePlayer oplayer = NiftyBukkit.getPlugin().getServer().getOfflinePlayer(uuid);

				if (oplayer != null) {
					JsonObject json = new JsonObject();
					json.addProperty("id", oplayer.getUniqueId().toString());
					json.addProperty("name", oplayer.getName());
					found = GSON.fromJson(json.toString(), MojangProfile.class);
				}
			}
		}

		if (found == null) {
			try {
				long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();
				if (wait > 0) Thread.sleep(wait);
				UUIDSearchResult[] results = GSON.fromJson(HTTP.get(new URL(StringUtil.format("https://api.mojang.com/user/profiles/{0}/names", uuid.toString().replace("-", "")))), UUIDSearchResult[].class);
				LAST_HTTP_REQUEST = System.currentTimeMillis();

				if (results != null && results.length > 0) {
					UUIDSearchResult result = results[0];
					JsonObject json = new JsonObject();
					json.addProperty("id", uuid.toString());
					json.addProperty("name", result.getName());
					found = GSON.fromJson(json.toString(), MojangProfile.class);
					CACHE.add(found);
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		if (found == null)
			throw new ProfileNotFoundException(ProfileNotFoundException.TYPE.UNIQUE_ID, uuid);
		else
			return found;
	}

	@SuppressWarnings("unused")
	private static class UUIDSearchResult {

		private String name;
		private int changedToAt;

		public int getChangedToAt() {
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
			UUID uuid = event.getPlayer().getUniqueId();
			String name = event.getPlayer().getName();

			if (NiftyBukkit.getBungeeHelper().isOnline()) {
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