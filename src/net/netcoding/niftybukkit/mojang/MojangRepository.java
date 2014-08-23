package net.netcoding.niftybukkit.mojang;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.JsonObject;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import org.bukkit.entity.Player;
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

	private static URL getProfilesUrl() {
		try {
			return new URL("https://api.mojang.com/profiles/minecraft");
		} catch (MalformedURLException mue) { }

		return null;
	}

	public MojangProfile searchByExactPlayer(Player player) throws ProfileNotFoundException {
		if (player == null) throw ProfileNotFoundException.InvalidPlayer();
		return searchByExactUsername(player.getName());
	}

	public MojangProfile searchByExactUsername(String username) throws ProfileNotFoundException {
		MojangProfile[] profiles = searchByUsername(username);

		if (profiles.length > 0) {
			for (MojangProfile profile : profiles) {
				if (profile.getName().equalsIgnoreCase(username))
					return profile;
			}
		}

		throw ProfileNotFoundException.InvalidUsername(username);
	}

	public MojangProfile[] searchByPlayer(Player... players) throws ProfileNotFoundException {
		return searchByPlayer(Arrays.asList(players));
	}

	public MojangProfile[] searchByPlayer(List<Player> players) throws ProfileNotFoundException {
		List<String> usernames = new ArrayList<>();

		for (Player player : players) {
			if (player != null)
				usernames.add(player.getName());
		}

		return searchByUsername(usernames);
	}

	public MojangProfile[] searchByUsername(String... usernames) throws ProfileNotFoundException {
		return searchByUsername(Arrays.asList(usernames));
	}

	public MojangProfile[] searchByUsername(List<String> usernames) throws ProfileNotFoundException {
		if (ListUtil.isEmpty(usernames)) throw ProfileNotFoundException.InvalidUsernames(usernames);
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
								if (profile.getName().toLowerCase().equals(criteriaName)) found = profile;
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
					Player player = BukkitHelper.findPlayer(name);

					if (player != null) {
						JsonObject json = new JsonObject();
						json.addProperty("name", player.getName());
						json.addProperty("id", player.getUniqueId().toString());
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
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		if (profiles.size() == 0)
			throw ProfileNotFoundException.InvalidUsernames(usernames);
		else
			return profiles.toArray(new MojangProfile[profiles.size()]);
	}

	public MojangProfile searchByExactUUID(final UUID uuid) throws ProfileNotFoundException {
		if (uuid == null) throw ProfileNotFoundException.InvalidUUID(uuid);
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
				for (Player player : NiftyBukkit.getPlugin().getServer().getOnlinePlayers()) {
					MojangProfile profile = this.searchByExactPlayer(player);

					if (profile.getUniqueId().equals(uuid)) {
						found = profile;
						break;
					}
				}
			}
		}

		if (found == null) {
			try {
				long wait = LAST_HTTP_REQUEST + 100 - System.currentTimeMillis();
				if (wait > 0) Thread.sleep(wait);
				UUIDSearchResult result = GSON.fromJson(HTTP.post(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""))), UUIDSearchResult.class);
				LAST_HTTP_REQUEST = System.currentTimeMillis();

				if (result != null) {
					JsonObject json = new JsonObject();
					json.addProperty("name", result.getName());
					json.addProperty("id", result.getUniqueId());
					found = GSON.fromJson(json.toString(), MojangProfile.class);
					CACHE.add(found);
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		if (found == null)
			throw ProfileNotFoundException.InvalidUUID(uuid);
		else
			return found;
	}

	@SuppressWarnings("unused")
	private static class UUIDSearchResult {

		private String id;
		private String name;
		private UUIDProperties properties;

		public String getUniqueId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}

		private static class UUIDProperties {

			private String name;
			private String value;
			private String signature;

			public String getName() {
				return this.name;
			}

			public String getValue() {
				return this.value;
			}

			public String getSignature() {
				return this.value;
			}

		}

	}

	private static class RepositoryListener extends BukkitListener {

		public RepositoryListener() {
			super(NiftyBukkit.getPlugin());
		}

		@EventHandler
		public void onPlayerPostLogin(PlayerPostLoginEvent event) {
			UUID uuid = event.getPlayer().getUniqueId();
			String name = event.getPlayer().getName();

			if (NiftyBukkit.getBungeeHelper().isOnline()) {
				MojangProfile profile = NiftyBukkit.getMojangRepository().searchByExactPlayer(event.getPlayer());
				uuid = profile.getUniqueId();
				name = profile.getName();
			}

			for (MojangProfile profile : CACHE) {
				if (profile.getUniqueId().equals(uuid) || profile.getName().equalsIgnoreCase(name))
					CACHE.remove(profile);
			}
		}
		
	}

}