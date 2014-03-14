package net.netcoding.niftybukkit.mojang;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.com.google.gson.Gson;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.database.MySQL;
import net.netcoding.niftybukkit.database.ResultCallback;
import net.netcoding.niftybukkit.http.HttpBody;
import net.netcoding.niftybukkit.http.HttpClient;
import net.netcoding.niftybukkit.http.HttpHeader;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.mojang.exceptions.ProfileNotFoundException;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.entity.Player;

public class ProfileRepository {

	private static final int MAX_PAGES_TO_CHECK = 100;
	private static final transient Gson gson = new Gson();
	private static final transient HttpClient httpClient = new HttpClient();
	private static final transient MojangProfileCache profileCache = new MojangProfileCache();
	private static final transient MySQL mysql = NiftyBukkit.getMySQL();

	static {
		if (!NiftyBukkit.isMysqlMode()) {
			try {
				profileCache.init();
			} catch (InvalidConfigurationException ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
				profileCache.delete();
			}
		}
	}

	public void save() {
		if (!NiftyBukkit.isMysqlMode()) {
			try {
				if (profileCache.exists())
					profileCache.save();
			} catch (InvalidConfigurationException ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}
	}

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
		ConcurrentList<ProfileCriteria> criterion = new ConcurrentList<>();
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
		}

		for (ProfileCriteria criteria : criterion) {
			if (NiftyBukkit.isMysqlMode()) {
				try {
					String uuid = mysql.query("SELECT `uuid` FROM `ndb_uuids` WHERE `user` = ?;", new ResultCallback<String>() {
						@Override
						public String handle(ResultSet result) throws SQLException {
							return result.next() ? result.getString("uuid") : null;
						}
					}, criteria.getName());

					if (uuid != null) {
						profiles.add(new MojangProfile(criteria.getName(), uuid));
						criterion.remove(criteria);
					}
				} catch (SQLException ex) {
					NiftyBukkit.getPlugin().getLog().console(ex);
					break;
				}
			} else {
				if (profileCache.exists()) {
					String uuid = profileCache.getUUID(criteria.getName());

					if (uuid != null) {
						profiles.add(new MojangProfile(criteria.getName(), uuid));
						criterion.remove(criteria);
					}
				} else
					break;
			}
		}

		if (criterion.size() > 0) {
			try {
				HttpBody body = new HttpBody(gson.toJson(criterion));
				List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));

				for (int i = 1; i <= MAX_PAGES_TO_CHECK; i++) {
					NameSearchResult result = gson.fromJson(httpClient.post(new URL("https://api.mojang.com/profiles/page/" + i), body, headers), NameSearchResult.class);
					if (result.getSize() == 0) break;
					temporary.addAll(Arrays.asList(result.getProfiles()));
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		for (MojangProfile profile : temporary) {
			if (NiftyBukkit.isMysqlMode()) {
				try {
					mysql.update("INSERT IGNORE INTO `ndb_uuids` (`uuid`, `user`) VALUES (?, ?);", profile.getUniqueId(), profile.getName());
				} catch (SQLException ex) {
					NiftyBukkit.getPlugin().getLog().console(ex);
					break;
				}
			} else {
				if (!profileCache.getUUIDs().contains(profile.getUniqueId()))
					profileCache.add(profile);
			}

			if (!profiles.contains(profile)) profiles.add(profile);
		}

		migrate(temporary);

		if (profiles.size() == 0)
			throw ProfileNotFoundException.InvalidUsernames(usernames);
		else
			return profiles.toArray(new MojangProfile[profiles.size()]);
	}

	public MojangProfile searchByExactUUID(final String uuid) throws ProfileNotFoundException {
		if (uuid == null) throw ProfileNotFoundException.InvalidUUID(uuid);
		MojangProfile profile = null;

		if (NiftyBukkit.isMysqlMode()) {
			try {
				profile = mysql.query("SELECT `user` FROM `ndb_uuids` WHERE `uuid` = ?;", new ResultCallback<MojangProfile>() {
					@Override
					public MojangProfile handle(ResultSet result) throws SQLException {
						List<String> names = new ArrayList<>();
						while (result.next()) names.add(result.getString("user"));
						return names.size() > 0 ? new MojangProfile(names.get(0), uuid) : null;
					}
				}, uuid);
			} catch (SQLException ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		} else {
			if (profileCache.exists()) {
				if (profileCache.getUUIDs().contains(uuid))
					profile = new MojangProfile(profileCache.getUsername(uuid), uuid);
			}
		}

		if (profile == null) {
			try {
				List<HttpHeader> headers = new ArrayList<HttpHeader>(Arrays.asList(new HttpHeader("Content-Type", "application/json")));
				UUIDSearchResult result = gson.fromJson(httpClient.post(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid), headers), UUIDSearchResult.class);

				if (result != null) {
					profile = new MojangProfile(result.getUniqueId(), result.getName());

					if (NiftyBukkit.isMysqlMode()) {
						try {
							mysql.update("INSERT IGNORE INTO `ndb_uuids` (`uuid`, `user`) VALUES (?, ?);", profile.getUniqueId(), profile.getName());
						} catch (SQLException ex) {
							NiftyBukkit.getPlugin().getLog().console(ex);
						}
					} else {
						if (profileCache.exists()) {
							if (profileCache.getUUIDs().contains(uuid))
								profileCache.add(profile);
						}
					}
				}
			} catch (Exception ex) {
				NiftyBukkit.getPlugin().getLog().console(ex);
			}
		}

		migrate(profile);

		if (profile == null)
			throw ProfileNotFoundException.InvalidUUID(uuid);
		else
			return profile;
	}

	private static void migrate(final MojangProfile moveMe) {
		migrate(Arrays.asList(moveMe));
	}

	private static void migrate(final List<MojangProfile> moveMe) {
		if (NiftyBukkit.isMysqlMode()) {
			if (profileCache.exists()) {
				NiftyBukkit.getPlugin().getServer().getScheduler().runTaskAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
					@Override
					public void run() {
						List<String> uuids = profileCache.getUUIDs();

						for (MojangProfile profile : moveMe) {
							if (!uuids.contains(profile.getUniqueId()))
								uuids.add(profile.getUniqueId());
						}

						for (String uuid : uuids) {
							try {
								mysql.update("INSERT IGNORE INTO `ndb_uuids` (`uuid`, `user`) VALUES (?, ?);", uuid, profileCache.getUsername(uuid));
							} catch (SQLException ex) {
								NiftyBukkit.getPlugin().getLog().console(ex);
								break;
							}
						}

						profileCache.delete();
					}
				});
			}
		}
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