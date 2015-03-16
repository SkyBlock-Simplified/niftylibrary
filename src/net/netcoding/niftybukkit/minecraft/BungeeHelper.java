package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.events.BungeeLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerJoinEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerLeaveEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeServerLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeServerUnloadedEvent;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.ByteUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class BungeeHelper extends BukkitHelper implements PluginMessageListener {

	public static final String BUNGEE_CHANNEL = "BungeeCord";
	public static final String NIFTY_CHANNEL = "NiftyBungee";
	private static final ConcurrentHashMap<Class<?>, Integer> LOADED_LISTENERS = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, BungeeServer> SERVERS = new ConcurrentHashMap<>();
	private static boolean BUNGEE_DETECTED = false;
	private static boolean BUNGEE_ONLINEMODE = false;
	private static boolean LOADED_ONCE = false;
	private static boolean LOADED_EVENTONCE = false;
	private static final transient Gson GSON = new Gson();
	private final transient BungeeListener listener;
	private final String channel;

	static {
		new LogoutListener();
	}

	public BungeeHelper(JavaPlugin plugin) {
		this(plugin, BUNGEE_CHANNEL, null);
	}

	public BungeeHelper(JavaPlugin plugin, BungeeListener listener) {
		this(plugin, listener, false);
	}

	public BungeeHelper(JavaPlugin plugin, BungeeListener listener, boolean register) {
		this(plugin, BUNGEE_CHANNEL, listener, register);
	}

	public BungeeHelper(JavaPlugin plugin, String channel) {
		this(plugin, BUNGEE_CHANNEL, null);
	}

	public BungeeHelper(JavaPlugin plugin, String channel, boolean register) {
		this(plugin, channel, null, register);
	}

	public BungeeHelper(JavaPlugin plugin, String channel, BungeeListener listener) {
		this(plugin, BUNGEE_CHANNEL, listener, false);
	}

	public BungeeHelper(JavaPlugin plugin, String channel, BungeeListener listener, boolean register) {
		super(plugin);
		if (StringUtil.isEmpty(channel)) throw new IllegalArgumentException("A channel name must be passed when instantiating an instance of BungeeHelper!");

		if (LOADED_ONCE) {
			if (!this.isDetected())
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class until {0} has been found!", BUNGEE_CHANNEL));
			else if (channel.equals(NIFTY_CHANNEL))
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class using the {0} channel!", NIFTY_CHANNEL));
		} else
			LOADED_ONCE = true;

		this.channel = channel;
		this.listener = listener;
		if (register) this.register();
	}

	public void connect(MojangProfile profile, String targetServer) {
		this.write(this.getFirstProfile(), BUNGEE_CHANNEL, "ConnectOther", profile.getName(), targetServer);
	}

	public void forward(String subChannel, Object... data) {
		this.forward("ALL", subChannel, data);
	}

	public void forward(String targetServer, String subChannel, Object... data) {
		this.forward(this.getFirstProfile(), targetServer, subChannel, data);
	}

	public void forward(MojangProfile profile, String subChannel, Object... data) {
		this.forward(profile, "ALL", subChannel, data);
	}

	public void forward(MojangProfile profile, String targetServer, String subChannel, Object... data) {
		if (!this.isDetected()) throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
		if (StringUtil.isEmpty(targetServer)) throw new IllegalArgumentException("Target server cannot be null!");
		if (StringUtil.isEmpty(subChannel)) throw new IllegalArgumentException("Sub channel cannot be null!");
		if (subChannel.equalsIgnoreCase(NIFTY_CHANNEL)) throw new IllegalArgumentException(StringUtil.format("You cannot forward to {0} channels!", NIFTY_CHANNEL));
		if (subChannel.matches("^GetServers?|Player(?:Count|List)|UUID(?:Other)?|(?:Server)?IP$")) throw new IllegalArgumentException(StringUtil.format("The {{0}} {1} channels are handled automatically; manual forwarding disabled!", subChannel, BUNGEE_CHANNEL));
		byte[] forward = ByteUtil.toByteArray(data);
		byte[] output = ByteUtil.toByteArray("Forward", targetServer, subChannel, (short)forward.length, forward);

		if (profile.getOfflinePlayer().isOnline())
			profile.getOfflinePlayer().getPlayer().sendPluginMessage(this.getPlugin(), BUNGEE_CHANNEL, output);
	}

	public String getChannel() {
		return this.channel;
	}

	private MojangProfile getFirstProfile() {
		return this.getServer().getPlayerCount() > 0 ? this.getServer().getPlayerList().iterator().next() : null;
	}

	public int getMaxPlayers() {
		return this.isDetected() ? this.getMaxPlayers(this.getServerName()) : this.getPlugin().getServer().getMaxPlayers();
	}

	public int getMaxPlayers(String serverName) {
		if (this.isDetected()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				int maxCount = 0;

				for (BungeeServer server : SERVERS.values())
					maxCount += server.getMaxPlayers();

				return maxCount;
			}

			BungeeServer server = SERVERS.get(serverName);
			if (server != null) return server.getMaxPlayers();
			throw new RuntimeException(StringUtil.format("The server name {0} does not exist!", serverName));
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	@SuppressWarnings("deprecation")
	public int getPlayerCount() {
		return this.isDetected() ? this.getPlayerCount(this.getServerName()) : this.getPlugin().getServer().getOnlinePlayers().length;
	}

	public int getPlayerCount(String serverName) {
		if (this.isDetected()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				int playerCount = 0;

				for (BungeeServer server : SERVERS.values())
					playerCount += server.getPlayerCount();

				return playerCount;
			}

			BungeeServer server = SERVERS.get(serverName);
			if (server != null) return server.getPlayerCount();
			throw new RuntimeException(StringUtil.format("The server name {0} does not exist!", serverName));

		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	@SuppressWarnings("deprecation")
	public Set<MojangProfile> getPlayerList() {
		if (this.isDetected())
			return this.getPlayerList(this.getServerName());

		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(NiftyBukkit.getMojangRepository().searchByPlayer(this.getPlugin().getServer().getOnlinePlayers()))));
	}

	public Set<MojangProfile> getPlayerList(String serverName) {
		if (this.isDetected()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				Set<MojangProfile> playerNames = new HashSet<>();
				for (BungeeServer server : SERVERS.values()) playerNames.addAll(server.getPlayerList());
				return Collections.unmodifiableSet(playerNames);
			}

			BungeeServer server = SERVERS.get(serverName);
			if (server != null) return server.getPlayerList();
			throw new RuntimeException(StringUtil.format("The server name {0} does not exist!", serverName));
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getPlayerServer(MojangProfile profile) {
		if (this.isDetected()) {
			for (BungeeServer server : this.getServers()) {
				if (server.getPlayerList().contains(profile))
					return server;
			}

			throw new RuntimeException(StringUtil.format("Unable to locate the server of {0}!", profile));
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getServer() {
		if (this.isDetected()) {
			BungeeServer currentServer = null;

			for (BungeeServer server : SERVERS.values()) {
				if (server.isCurrentServer()) {
					currentServer = server;
					break;
				}
			}

			return currentServer;
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getServer(String serverName) {
		if (this.isDetected())
			return SERVERS.get(serverName);

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public String getServerName() {
		for (BungeeServer server : SERVERS.values()) {
			if (server.isCurrentServer())
				return server.getName();
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public Set<String> getServerNames() {
		if (this.isDetected()) {
			Set<String> serverNames = new HashSet<>();
			for (BungeeServer server : SERVERS.values()) serverNames.add(server.getName());
			return Collections.unmodifiableSet(serverNames);
		}

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public Set<BungeeServer> getServers() {
		if (this.isDetected())
			return Collections.unmodifiableSet(new HashSet<>(SERVERS.values()));

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public final boolean isDetected() {
		return BUNGEE_DETECTED;
	}

	public final boolean isOnlineMode() {
		return BUNGEE_ONLINEMODE;
	}

	public boolean isPlayerOnline(MojangProfile profile) {
		if (profile.getOfflinePlayer().isOnline())
			return true;
		else if (this.isDetected()) {
			for (BungeeServer server : this.getServers()) {
				if (server.getPlayerList().contains(profile))
					return true;
			}
		}

		return false;
	}

	public boolean isRegistered() {
		return this.isRegistered(this.getChannel());
	}

	private boolean isRegistered(String channel) {
		return this.getPlugin().getServer().getMessenger().isIncomingChannelRegistered(this.getPlugin(), channel) && this.getPlugin().getServer().getMessenger().isOutgoingChannelRegistered(this.getPlugin(), channel);
	}

	public void message(MojangProfile toProfile, String message) {
		this.message(this.getFirstProfile(), toProfile, message);
	}

	public void message(MojangProfile fromProfile, MojangProfile profile, String message) {
		this.write(fromProfile, BUNGEE_CHANNEL, "Message", profile.getName(), message);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!this.isRegistered()) return;
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();

		if (channel.equals(NIFTY_CHANNEL)) {
			try {
				PluginManager manager = this.getPlugin().getServer().getPluginManager();

				if (subChannel.equals("BungeeInfo"))
					BUNGEE_ONLINEMODE = input.readBoolean();
				else if (subChannel.equals("GetServers")) {
					BUNGEE_DETECTED = false;
					SERVERS.clear();
					int count = input.readInt();

					for (int i = 0; i < count; i++) {
						final BungeeServer server = new BungeeServer(input.readUTF());
						server.setAddress(input.readUTF(), input.readInt());
						SERVERS.put(server.getName(), server);
						manager.callEvent(new BungeeServerLoadedEvent(server));
					}

					BUNGEE_DETECTED = true;
				} else {
					if (!this.isDetected()) return;
					final BungeeServer server = this.getServer(input.readUTF());

					if (subChannel.equals("ServerInfo")) {
						server.setOnline(input.readBoolean());

						if (server.isOnline()) {
							server.setMotd(input.readUTF());
							server.setGameVersion(input.readUTF());
							server.setProtocolVersion(input.readInt());
							server.setMaxPlayers(input.readInt());
							boolean updatePlayers = input.readBoolean();
							server.playerList.clear();

							if (updatePlayers) {
								int count = input.readInt();

								for (int i = 0; i < count; i++) {
									JsonObject json = new JsonObject();
									String[] parts = input.readUTF().split(",");
									json.addProperty("id", parts[0]);
									json.addProperty("name", parts[1]);
									json.addProperty("ip", parts[2]);
									json.addProperty("port", Integer.valueOf(parts[3]));
									server.playerList.add(GSON.fromJson(json.toString(), MojangProfile.class));
								}
							}

							if (!server.loadedOnce) {
								server.loadedOnce = true;
								manager.callEvent(new BungeeServerLoadedEvent(server));
							}
						} else {
							server.reset();
							manager.callEvent(new BungeeServerUnloadedEvent(server));
						}

						if (!LOADED_EVENTONCE) {
							int loaded = 0;

							for (BungeeServer serv : SERVERS.values())
								loaded += serv.loadedOnce ? 1 : 0;

							if (loaded == SERVERS.size()) {
								LOADED_EVENTONCE = true;
								manager.callEvent(new BungeeLoadedEvent(SERVERS.values()));
							}
						}
					} else if (subChannel.startsWith("Player")) {
						String[] parts = input.readUTF().split(",");
						UUID uniqueId = UUID.fromString(parts[0]);

						if (subChannel.endsWith("Join")) {
							JsonObject json = new JsonObject();
							json.addProperty("id", parts[0]);
							json.addProperty("name", parts[1]);
							json.addProperty("ip", parts[2]);
							json.addProperty("port", Integer.valueOf(parts[3]));
							MojangProfile profile = GSON.fromJson(json.toString(), MojangProfile.class);
							server.playerList.add(profile);
							manager.callEvent(new BungeePlayerJoinEvent(server, profile));
						} else if (subChannel.endsWith("Leave")) {
							for (MojangProfile profile : server.playerList) {
								if (profile.getUniqueId().equals(uniqueId)) {
									server.playerList.remove(profile);
									manager.callEvent(new BungeePlayerLeaveEvent(server, profile));
									break;
								}
							}
						}
					}
				}
			} catch (IllegalStateException isex) {
			} catch (Exception ex) {
				this.getLog().console(ex);
			}
		} else {
			if (channel.equals(BUNGEE_CHANNEL)) {
				if (subChannel.matches("^GetServers?|Player(?:Count|List)|UUID(?:Other)?|(?:Server)?IP$"))
					return;
			}

			if (channel.equals(this.getChannel())) {
				if (this.listener != null) {
					try {
						this.listener.onMessageReceived(channel, player, message);
					} catch (IllegalStateException isex) {
					} catch (Exception ex) {
						this.getLog().console(ex);
					}
				}
			}
		}
	}

	public void register() {
		if (this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!this.getChannel().equals(BUNGEE_CHANNEL)) {
			if (!this.isRegistered(BUNGEE_CHANNEL)) {
				this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
				this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);
				LOADED_LISTENERS.put(this.getPlugin().getClass(), 1);
			} else
				LOADED_LISTENERS.put(this.getPlugin().getClass(), LOADED_LISTENERS.get(this.getPlugin().getClass()) + 1);
		}
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!this.getChannel().equals(BUNGEE_CHANNEL)) {
			int current = LOADED_LISTENERS.get(this.getPlugin().getClass());
			LOADED_LISTENERS.put(this.getPlugin().getClass(), current - 1);

			if (current == 0) {
				this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
				this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);
			}
		}
	}

	public void write(MojangProfile profile, String subChannel, Object... data) {
		this.write(profile, this.getChannel(), subChannel, data);
	}

	private void write(MojangProfile profile, String channel, String subChannel, Object... data) {
		if (!this.isDetected()) return;
		if (StringUtil.isEmpty(subChannel)) throw new IllegalArgumentException("Sub channel cannot be null!");
		if (channel.equals("Forward")) return;
		List<Object> dataList = new ArrayList<>(Arrays.asList(data));
		dataList.add(0, subChannel);

		if (profile.getOfflinePlayer().isOnline())
			profile.getOfflinePlayer().getPlayer().sendPluginMessage(this.getPlugin(), channel, ByteUtil.toByteArray(dataList));
	}

	private static class LogoutListener extends BukkitListener {

		public LogoutListener() {
			super(NiftyBukkit.getPlugin());
		}

		@EventHandler
		public void onPlayerKick(PlayerKickEvent event) {
			System.out.println("quit: " + event.getPlayer().getName());
		}

		@EventHandler
		public void onPlayerLeave(PlayerQuitEvent event) {
			System.out.println("quit: " + event.getPlayer().getName());
		}
		
	}

}