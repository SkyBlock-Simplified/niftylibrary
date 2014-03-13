package net.netcoding.niftybukkit.mojang;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.com.google.gson.Gson;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.ResultCallback;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.entity.Player;

public class ProfileRepository {

	private static final int MAX_PAGES_TO_CHECK = 100;
	private static final transient Gson gson = new Gson();
	private static final transient HttpClient httpClient;

	static {
		httpClient = new HttpClient();
	}

	public static MojangProfile searchByExactPlayer(Player player) {
		return searchByPlayer(player)[0];
	}

	public static MojangProfile searchByExactUsername(Player player) {
		return searchByExactUsername(player.getName());
	}

	public static MojangProfile searchByExactUsername(String username) {
		MojangProfile[] profiles = searchByUsername(username);

		if (profiles.length > 0) {
			for (MojangProfile profile : profiles) {
				if (profile.getName().equalsIgnoreCase(username))
					return profile;
			}
		}

		return null;
	}

	public static MojangProfile[] searchByPlayer(Player... players) {
		return searchByPlayer(Arrays.asList(players));
	}

	public static MojangProfile[] searchByPlayer(List<Player> players) {
		List<String> usernames = new ArrayList<>();

		for (int i = 0; i < usernames.size(); i++) {
			if (players.get(i) != null)
				usernames.add(players.get(i).getName());
		}

		return searchByUsername(usernames);
	}

	public static MojangProfile[] searchByUsername(String... usernames) {
		return searchByUsername(Arrays.asList(usernames));
	}

	public static MojangProfile[] searchByUsername(List<String> usernames) {
		ConcurrentList<ProfileCriteria> criterion = new ConcurrentList<>();
		List<MojangProfile> profiles = new ArrayList<>();
		MySQL mysql = NiftyBukkit.getMySQL();
		MojangProfileCache profileCache = new MojangProfileCache();

		if (!NiftyBukkit.isMysqlMode()) {
			try {
				profileCache.init();
			} catch (InvalidConfigurationException ex) {
				profileCache.getLog().console(ex);
				profileCache.delete();
			}
		}

		for (String username : usernames) {
			if (!"".equals(username))
				criterion.add(new ProfileCriteria(username));
		}

		if (NiftyBukkit.getPlugin().getServer().getOnlineMode()) {
			for (ProfileCriteria criteria : criterion) {
				Player player = BukkitHelper.matchPlayer(criteria.getName());

				if (player != null) {
					profiles.add(new MojangProfile(player.getName(), player.getUniqueId().toString()));
					criterion.remove(criteria);
				}
			}
		}

		for (ProfileCriteria criteria : criterion) {
			if (NiftyBukkit.isMysqlMode()) {
				try {
					String uuid = mysql.query("SELECT `uuid` FROM `ndb_uuids` WHERE `user` = ? GROUP BY `time` LIMIT 1;", new ResultCallback<String>() {
						@Override
						public String handle(ResultSet result) throws SQLException {
							return result.next() ? result.getString("uuid") : null;
						}
					});

					if (uuid != null) {
						MojangProfile profile = new MojangProfile(criteria.getName(), uuid);
						profile.setNames(mysql.query("SELECT `user` FROM `ndb_uuids` WHERE `uuid` = ?;", new ResultCallback<Set<String>>() {
							@Override
							public Set<String> handle(ResultSet result) throws SQLException {
								Set<String> names = new HashSet<>();
								while (result.next()) names.add(result.getString("user"));
								return names;
							}
						}, uuid));
						profiles.add(profile);
						criterion.remove(criteria);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else {
				if (profileCache.exists()) {
					String uuid = profileCache.findUUID(criteria.getName());

					if (!"".equals(uuid)) {
						MojangProfile profile = new MojangProfile(criteria.getName(), uuid);
						profile.setNames(profileCache.getNames(uuid));
						profiles.add(profile);
						criterion.remove(criteria);
					}
				} else
					break;
			}
		}

		try {
			HttpBody body = new HttpBody(gson.toJson(criterion));
			List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));

			for (int i = 1; i <= MAX_PAGES_TO_CHECK; i++) {
				ProfileSearchResult result = search(new URL("https://api.mojang.com/profiles/page/" + i), body, headers);
				if (result.getSize() == 0) break;
				profiles.addAll(Arrays.asList(result.getProfiles()));
			}
		} catch (Exception ex) { }

		for (MojangProfile profile : profiles) {
			if (NiftyBukkit.isMysqlMode()) {
				try {
					mysql.update("INSERT IGNORE INTO `ndb_users` (`uuid`, `user`, `time`) VALUES (?, ?, UNIX_TIMESTAMP());", profile.getUniqueId(), profile.getName());
				} catch (SQLException ex) { }
			} else
				profileCache.add(profile);

			profiles.add(profile);
		}

		if (!NiftyBukkit.isMysqlMode()) {
			try {
				profileCache.save();
			} catch (InvalidConfigurationException ex) {
				profileCache.getLog().console(ex);
				profileCache.delete();
			}
		}

		return profiles.toArray(new MojangProfile[profiles.size()]);
	}

	public static MojangProfile searchByExactUUID(String uuid) {
		return null; // TODO
	}

	private static ProfileSearchResult search(URL url, HttpBody body, List<HttpHeader> headers) throws IOException {
		return gson.fromJson(httpClient.post(url, body, headers), ProfileSearchResult.class);
	}

	private static class ProfileSearchResult {

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