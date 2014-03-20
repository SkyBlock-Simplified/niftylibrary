package net.netcoding.niftybukkit.mojang;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.com.google.gson.Gson;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.minecraft.BungeeHelper;
import net.netcoding.niftybukkit.minecraft.BungeeServer;
import net.netcoding.niftybukkit.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import org.bukkit.entity.Player;

public class ProfileRepository {

	private static final int MAX_PAGES_TO_CHECK = 100;
	private static final transient Gson gson = new Gson();
	private static final transient HttpClient httpClient = new HttpClient();
	private static final ConcurrentSet<MojangProfile> profileCache = new ConcurrentSet<>();

	public MojangProfile searchByExactPlayer(Player player) throws ProfileNotFoundException {
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
		if (usernames == null || usernames.size() == 0) throw ProfileNotFoundException.InvalidUsernames(usernames);
		ConcurrentSet<ProfileCriteria> criterion = new ConcurrentSet<>();
		List<MojangProfile> profiles = new ArrayList<>();
		final List<MojangProfile> temporary = new ArrayList<>();

		for (String username : usernames) {
			if (StringUtil.notEmpty(username))
				criterion.add(new ProfileCriteria(username));
		}

		if (NiftyBukkit.getPlugin().getServer().getOnlineMode()) {
			for (ProfileCriteria criteria : criterion) {
				Player player = BukkitHelper.findPlayer(criteria.getName());

				if (player != null) {
					temporary.add(new MojangProfile(player.getName(), player.getUniqueId().toString()));
					criterion.remove(criteria);
				}
			}
		} else if (BungeeHelper.bungeeOnline()) {
			BungeeHelper helper = new BungeeHelper(NiftyBukkit.getPlugin());

			for (ProfileCriteria criteria : criterion) {
				for (BungeeServer server : helper.getServers()) {
					if (server.isOnline()) {
						if (server.getPlayerList().contains(criteria.getName())) {
							// TODO: Store Mojang Profile
							criterion.remove(criteria);
							break;
						}
					}
				}
			}
		}

		for (ProfileCriteria criteria : criterion) {
			for (MojangProfile profile : profileCache) {
				if (profile.hasExpired()) {
					profileCache.remove(profile);
					continue;
				}

				if (profile.getName().equalsIgnoreCase(criteria.getName())) {
					profiles.add(profile);
					criterion.remove(criteria);
					break;
				}
			}
		}

		if (criterion.size() > 0) {
			try {
				HttpBody body = new HttpBody(gson.toJson(criterion));
				List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));

				for (int i = 1; i <= MAX_PAGES_TO_CHECK; i++) {
					NameSearchResult result = gson.fromJson(httpClient.post(new URL("https://api.mojang.com/profiles/page/" + i), body, headers), NameSearchResult.class);
					if (result.getSize() == 0) break;
					profiles.addAll(Arrays.asList(result.getProfiles()));
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		if (profiles.size() == 0)
			throw ProfileNotFoundException.InvalidUsernames(usernames);
		else
			return profiles.toArray(new MojangProfile[profiles.size()]);
	}

	public MojangProfile searchByExactUUID(final String uuid) throws ProfileNotFoundException {
		if (uuid == null) throw ProfileNotFoundException.InvalidUUID(uuid);
		MojangProfile found = null;

		for (MojangProfile profile : profileCache) {
			if (profile.hasExpired()) {
				profileCache.remove(profile);
				continue;
			}

			if (profile.getUniqueId().equalsIgnoreCase(uuid)) {
				found = profile;
				break;
			}
		}

		if (found == null) {
			try {
				List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));
				UUIDSearchResult result = gson.fromJson(httpClient.post(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid), headers), UUIDSearchResult.class);
				if (result != null) found = new MojangProfile(result.getUniqueId(), result.getName());
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

	private static class NameSearchResult {

		private MojangProfile[] profiles;
		private int size;

		public MojangProfile[] getProfiles() {
			return profiles;
		}

		public int getSize() {
			return size;
		}

	}

	@SuppressWarnings("unused")
	private static class ProfileCriteria {

		private final String name;
		private final String agent = "minecraft";

		public ProfileCriteria(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

}