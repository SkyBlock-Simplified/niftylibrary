package net.netcoding.niftybukkit.minecraft.messages;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BukkitServerPingEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerJoinEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerLeaveEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeServerLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.PlayerDisconnectEvent;
import net.netcoding.niftybukkit.minecraft.events.PlayerNameChangeEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.minecraft.scheduler.MinecraftScheduler;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.util.ByteUtil;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BungeeHelper extends BukkitHelper implements PluginMessageListener {

	public static final String BUNGEE_CHANNEL = "BungeeCord";
	public static final String NIFTY_CHANNEL = "NiftyBungee";
	private static final ConcurrentHashMap<String, BungeeServer> SERVERS = new ConcurrentHashMap<>();
	private static Socket SOCKET = null;
	private static boolean SOCKET_LISTENING = false;
	private static boolean SOCKET_TRIED_ONCE = false;
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

		if (StringUtil.isEmpty(channel))
			throw new IllegalArgumentException("A channel name must be passed when instantiating an instance of BungeeHelper!");

		if (LOADED_ONCE) {
			if (!this.isDetected())
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class until {0} has been found!", BUNGEE_CHANNEL));
			else if (channel.equals(NIFTY_CHANNEL))
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class using the {0} channel!", NIFTY_CHANNEL));
		} else
			LOADED_ONCE = true;

		this.channel = channel;
		this.listener = listener;

		if (register)
			this.register();
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
		BukkitMojangProfile bukkitProfile = (BukkitMojangProfile)profile;

		if (bukkitProfile.getOfflinePlayer().isOnline())
			bukkitProfile.getOfflinePlayer().getPlayer().sendPluginMessage(this.getPlugin(), BUNGEE_CHANNEL, output);
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

	public int getPlayerCount() {
		return this.isDetected() ? this.getPlayerCount(this.getServerName()) : ListUtil.sizeOf(this.getPlugin().getServer().getOnlinePlayers());
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

	public Collection<BukkitMojangProfile> getPlayerList() {
		if (this.isDetected())
			return this.getPlayerList(this.getServerName());

		BukkitMojangProfile[] profiles = NiftyBukkit.getMojangRepository().searchByPlayer(this.getPlugin().getServer().getOnlinePlayers());
		return Collections.unmodifiableCollection(Arrays.asList(profiles));
	}

	public Collection<BukkitMojangProfile> getPlayerList(String serverName) {
		if (this.isDetected()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				Set<BukkitMojangProfile> playerNames = new HashSet<>();

				for (BungeeServer server : SERVERS.values())
					playerNames.addAll(server.getPlayerList());

				return Collections.unmodifiableCollection(playerNames);
			}

			BungeeServer server = SERVERS.get(serverName);
			if (server != null)
				return server.getPlayerList();

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
		if (((BukkitMojangProfile)profile).getOfflinePlayer().isOnline())
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

	private void handleMessage(String channel, byte[] message) {
		if (!this.isRegistered()) return;
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();
		PluginManager manager = this.getPlugin().getServer().getPluginManager();

		if (channel.equals(NIFTY_CHANNEL)) {
			try {
				if (subChannel.equals("BungeeInfo"))
					BUNGEE_ONLINEMODE = input.readBoolean();
				else if (subChannel.equals("GetServers")) {
					BUNGEE_DETECTED = false;
					SERVERS.clear();
					JsonParser parser = new JsonParser();
					int count = input.readInt();

					for (int i = 0; i < count; i++) {
						JsonObject json = parser.parse(input.readUTF()).getAsJsonObject();
						final BungeeServer server = new BungeeServer(json.get("name").getAsString());
						server.setAddress(json.get("ip").getAsString(), json.get("port").getAsInt());
						SERVERS.put(server.getName(), server);
					}

					BUNGEE_DETECTED = true;
				} else {
					if (!this.isDetected()) return;
					final BungeeServer server = this.getServer(input.readUTF());

					if (subChannel.equals("ServerInfo")) {
						server.setOnline(input.readBoolean());

						if (server.isOnline()) {
							server.setMotd(input.readUTF());
							server.setVersion(input.readUTF(), input.readInt());
							server.setMaxPlayers(input.readInt());
							server.getUnsafePlayerList().clear();
							int count = input.readInt();

							for (int i = 0; i < count; i++)
								server.getUnsafePlayerList().add(GSON.fromJson(input.readUTF(), BukkitMojangProfile.class));
						} else
							server.reset();

						server.loadedOnce = true;
						manager.callEvent(new BungeeServerLoadedEvent(server));

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
						BukkitMojangProfile profile = GSON.fromJson(input.readUTF(), BukkitMojangProfile.class);

						if (subChannel.endsWith("Join")) {
							server.getUnsafePlayerList().add(profile);
							manager.callEvent(new BungeePlayerJoinEvent(profile));
						} else if (subChannel.endsWith("Leave")) {
							if (server.isCurrentServer())
								server.playersLeft.add(profile);

							server.getUnsafePlayerList().remove(profile);
							manager.callEvent(new BungeePlayerLeaveEvent(profile));
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
				else if (subChannel.equals("PlayerUpdate")) {
					JsonObject json = new JsonParser().parse(input.readUTF()).getAsJsonObject();
					BukkitMojangProfile updatedProfile = GSON.fromJson(json.toString(), BukkitMojangProfile.class);
					BungeeServer server = SERVERS.get(input.readUTF());

					for (MojangProfile profile : server.getPlayerList()) {
						if (profile.equals(updatedProfile)) {
							server.getUnsafePlayerList().remove(profile);
							server.getUnsafePlayerList().add(updatedProfile);
							manager.callEvent(new PlayerNameChangeEvent(updatedProfile));
							break;
						}
					}

					return;
				}
			}

			if (channel.equals(this.getChannel())) {
				if (this.listener != null) {
					try {
						this.listener.onMessageReceived(channel, message);
					} catch (IllegalStateException isex) {
					} catch (Exception ex) {
						this.getLog().console(ex);
					}
				}
			}
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		this.handleMessage(channel, message);
	}

	public void register() {
		if (this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!this.getChannel().equals(BUNGEE_CHANNEL)) {
			if (!this.isRegistered(BUNGEE_CHANNEL)) {
				this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
				this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);
			}
		}
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (this.getChannel().equals(NIFTY_CHANNEL)) {
			this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
			this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);

			SOCKET_LISTENING = false;
			try {
				SOCKET.close();
			} catch (Exception ex) {
				getLog().console("Unable to close socket", ex);
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
		BukkitMojangProfile bukkitProfile = (BukkitMojangProfile)profile;

		if (bukkitProfile.getOfflinePlayer().isOnline())
			bukkitProfile.getOfflinePlayer().getPlayer().sendPluginMessage(this.getPlugin(), channel, ByteUtil.toByteArray(dataList));
	}

	private static class LogoutListener extends BukkitListener {

		public LogoutListener() {
			super(NiftyBukkit.getPlugin());
		}

		private void handleDisconnect(Player player, boolean kicked) {
			MojangProfile profile = null;

			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				for (MojangProfile left : NiftyBukkit.getBungeeHelper().getServer().getTotalPlayerList()) {
					if (((BukkitMojangProfile)left).belongsTo(player)) {
						profile = left;
						break;
					}
				}

				NiftyBukkit.getBungeeHelper().getServer().getUnsafePlayerList().remove(profile);
				NiftyBukkit.getBungeeHelper().getServer().playersLeft.remove(profile);

				if (NiftyBukkit.getBungeeHelper().getServer().getPlayerCount() == 0) {
					BUNGEE_ONLINEMODE = false;
					BUNGEE_DETECTED = false;
					SERVERS.clear();
				}
			} else
				profile = NiftyBukkit.getMojangRepository().searchByPlayer(player);

			this.getPlugin().getServer().getPluginManager().callEvent(new PlayerDisconnectEvent(profile, kicked));
		}

		private ServerSocket serverSocket;

		@EventHandler
		public void onServerListPing(final ServerListPingEvent event) {
			if (!SOCKET_TRIED_ONCE) {
				SOCKET_TRIED_ONCE = true;

				try {
					this.serverSocket = new ServerSocket(0);
					BukkitServerPingEvent pingEvent = new BukkitServerPingEvent(event);
					pingEvent.sendSpoofedVersion(StringUtil.format("{0} {1,number,#}", "NiftyPing", serverSocket.getLocalPort()), true);

					MinecraftScheduler.runAsync(new Runnable() {
						@Override
						public void run() {
							try {
								getLog().console("{0}: Waiting for NiftyPing", System.currentTimeMillis());
								Socket socket = serverSocket.accept();
								getLog().console("Accepted: {0}", socket);

								try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
									if ("NiftyPing".equals(inputStream.readUTF())) {
										String ip = inputStream.readUTF();
										int port = inputStream.readInt();
										getLog().console("Preparing: {0}:{1,number,#}", ip, port);
										SOCKET = new Socket(ip, port);
										getLog().console("Loaded Socket: {0}", socket);
										SOCKET.setKeepAlive(true);
										SOCKET.setSoTimeout(0);
										SOCKET_LISTENING = true;
										MinecraftScheduler.runAsync(new SocketRunnable());
									}
								}
							} catch (Exception ex) {
								getLog().console("Unable to accept socket connection!", ex);
							} finally {
								try {
									serverSocket.close();
								} catch (Exception ex) {
									getLog().console("Unable to close server socket", ex);
								}
							}
						}
					});
				} catch (Exception ex) {
					this.getLog().console("Unable to setup socket channel!");
				}
			}
		}

		@EventHandler
		public void onPlayerKick(PlayerKickEvent event) {
			this.handleDisconnect(event.getPlayer(), true);
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event) {
			this.handleDisconnect(event.getPlayer(), false);
		}

		private class SocketRunnable implements Runnable {

			@Override
			public void run() {
				try {
					try (DataInputStream inputStream = new DataInputStream(SOCKET.getInputStream())) {
						while (SOCKET_LISTENING) {
							System.out.println("made it x1");
							String channel = inputStream.readUTF();
							System.out.println("made it x2: " + channel);
							int length = inputStream.readInt();
							byte[] data = new byte[length];
							inputStream.read(data);
							NiftyBukkit.getBungeeHelper().handleMessage(channel, data);
						}
					}
				} catch (IOException ioex) {
					getLog().console(ioex);
					// TODO: Possibly Disable Socket
				}
			}

		}

	}

}