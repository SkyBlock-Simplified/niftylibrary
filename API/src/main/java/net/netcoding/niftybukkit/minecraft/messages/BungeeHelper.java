package net.netcoding.niftybukkit.minecraft.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.BukkitServerPingEvent;
import net.netcoding.niftybukkit.minecraft.events.bungee.BungeeLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.bungee.BungeeProfileJoinEvent;
import net.netcoding.niftybukkit.minecraft.events.bungee.BungeeProfileLeaveEvent;
import net.netcoding.niftybukkit.minecraft.events.bungee.BungeeServerLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.profile.ProfileNameChangeEvent;
import net.netcoding.niftybukkit.minecraft.events.profile.ProfileQuitEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.minecraft.scheduler.MinecraftScheduler;
import net.netcoding.niftycore.util.ByteUtil;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.ServerSocketWrapper;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class BungeeHelper extends BukkitHelper {

	static final transient Gson GSON = new Gson();
	public static final String BUNGEE_CHANNEL = "BungeeCord";
	private static final String NIFTY_CHANNEL = "NiftyBungee";
	private static BungeeHelper INSTANCE;
	private static final ConcurrentSet<ChannelWrapper> LISTENERS = new ConcurrentSet<>();
	private static final ConcurrentMap<String, BungeeServer> SERVERS = new ConcurrentMap<>();
	private static final ServerSocketWrapper BUKKIT_SOCKET_WRAPPER;
	private static String BUNGEE_SOCKET_IP = null;
	private static int BUNGEE_SOCKET_PORT = -1;
	private static boolean SOCKET_TRIED_ONCE = true;//false;
	private static boolean BUNGEE_DETECTED = false;
	private static boolean BUNGEE_ONLINEMODE = false;
	private static boolean LOADED_EVENTONCE = false;
	private final ChannelWrapper NIFTY_WRAPPER;

	static {
		ServerSocket socket = null;

		/*try {
			socket = new ServerSocket(0);
			socket.setSoTimeout(2000);
		} catch (IOException ioex) {
			NiftyBukkit.getPlugin().getLog().console("Unable to register socket!", ioex);
		}*/

		BUKKIT_SOCKET_WRAPPER = new ServerSocketWrapper(socket);
	}

	BungeeHelper() {
		super(NiftyBukkit.getPlugin());
		NIFTY_WRAPPER = new NiftyChannelWrapper(NiftyBukkit.getPlugin());
		NIFTY_WRAPPER.register();
		LISTENERS.add(NIFTY_WRAPPER);
	}

	public void connect(BukkitMojangProfile profile, String targetServer) {
		this.write(this.getFirstProfile(), BUNGEE_CHANNEL, "ConnectOther", profile.getName(), targetServer);
	}

	public void forward(String subChannel, Object... data) {
		this.forward("ALL", subChannel, data);
	}

	public void forward(String targetServer, String subChannel, Object... data) {
		this.forward(this.getFirstProfile(), targetServer, subChannel, data);
	}

	public void forward(BukkitMojangProfile profile, String subChannel, Object... data) {
		this.forward(profile, "ALL", subChannel, data);
	}

	public void forward(BukkitMojangProfile profile, String targetServer, String subChannel, Object... data) {
		if (!this.isDetected()) throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
		if (StringUtil.isEmpty(targetServer)) throw new IllegalArgumentException("Target server cannot be null!");
		if (StringUtil.isEmpty(subChannel)) throw new IllegalArgumentException("Sub channel cannot be null!");
		if (subChannel.equalsIgnoreCase(NIFTY_CHANNEL)) throw new IllegalArgumentException(StringUtil.format("You cannot forward to {0} channels!", NIFTY_CHANNEL));
		if (subChannel.matches("^GetServers?|Player(?:Count|List)|UUID(?:Other)?|(?:Server)?IP$")) throw new IllegalArgumentException(StringUtil.format("The {{0}} {1} channels are handled automatically; manual forwarding disabled!", subChannel, BUNGEE_CHANNEL));
		byte[] forward = ByteUtil.toByteArray(data);
		this.write(profile, BUNGEE_CHANNEL, ByteUtil.toByteArray("Forward", targetServer, subChannel, (short)forward.length, forward));
	}

	private BukkitMojangProfile getFirstProfile() {
		return this.getServer().getPlayerCount() > 0 ? this.getServer().getPlayerList().iterator().next() : null;
	}

	public static BungeeHelper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BungeeHelper();
		}
		return INSTANCE;
	}

	public int getMaxPlayers() {
		return this.isDetected() ? this.getMaxPlayers(this.getServerName()) : this.getPlugin().getServer().getMaxPlayers();
	}

	public int getMaxPlayers(String serverName) {
		if (this.isDetected()) {
			if ("ALL".equalsIgnoreCase(serverName)) {
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
			if ("ALL".equalsIgnoreCase(serverName)) {
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
			if ("ALL".equalsIgnoreCase(serverName)) {
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

		if ("ALL".equalsIgnoreCase(serverName))
			return this.getPlayerList();

		throw new UnsupportedOperationException(StringUtil.format("No {0} listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getPlayerServer(BukkitMojangProfile profile) {
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

	public static ServerSocketWrapper getSocketWrapper() {
		return BUKKIT_SOCKET_WRAPPER;
	}

	public static boolean isBungeeSocketListening() {
		return StringUtil.notEmpty(BUNGEE_SOCKET_IP) && BUNGEE_SOCKET_PORT > 0;
	}

	public final boolean isDetected() {
		return BUNGEE_DETECTED;
	}

	public final boolean isOnlineMode() {
		return BUNGEE_ONLINEMODE;
	}

	public boolean isPlayerOnline(BukkitMojangProfile profile) {
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

	public void message(BukkitMojangProfile toProfile, String message) {
		this.message(this.getFirstProfile(), toProfile, message);
	}

	public void message(BukkitMojangProfile fromProfile, BukkitMojangProfile profile, String message) {
		this.write(fromProfile, BUNGEE_CHANNEL, "Message", profile.getName(), message);
	}

	/**
	 * Registers a plugin message listener.
	 *
	 * @param plugin Plugin the listener is bound to.
	 * @param listener Channel listener implementation to send results to.
	 * @return Wrapped channel containing plugin, BungeeCord channel and listener.
	 */
	public ChannelWrapper register(JavaPlugin plugin, ChannelListener listener) {
		return this.register(plugin, BUNGEE_CHANNEL, listener);
	}

	/**
	 * Registers a plugin message listener.
	 *
	 * @param plugin Plugin the listener is bound to.
	 * @param channel Channel to listen to.
	 * @param listener Channel listener implementation to send results to.
	 * @return Wrapped channel containing plugin, channel and listener.
	 */
	public ChannelWrapper register(JavaPlugin plugin, String channel, ChannelListener listener) {
		if (plugin == null)
			throw new IllegalArgumentException("The plugin cannot be NULL!");

		if (StringUtil.isEmpty(channel))
			throw new IllegalArgumentException("The channel name cannot be NULL!");

		if (listener == null)
			throw new IllegalArgumentException("The listener cannot be NULL!");

		if (!this.isDetected())
			throw new UnsupportedOperationException(StringUtil.format("You cannot register a listener until {0} has been found!", BUNGEE_CHANNEL));

		if (NIFTY_CHANNEL.equalsIgnoreCase(channel))
			throw new UnsupportedOperationException(StringUtil.format("You cannot register a listener using the {0} channel!", NIFTY_CHANNEL));

		ChannelWrapper wrapper = new ChannelWrapper(plugin, channel, listener);

		if (LISTENERS.contains(wrapper))
			throw new IllegalArgumentException(StringUtil.format("The channel {0} is already registered for plugin {1}!", channel, plugin.getName()));

		wrapper.register();
		LISTENERS.add(wrapper);
		return wrapper;
	}

	void write(BukkitMojangProfile profile, String channel, String subChannel, Object... data) {
		if (!this.isDetected()) return;
		if (StringUtil.isEmpty(subChannel)) throw new IllegalArgumentException("Sub channel cannot be null!");
		if ("Forward".equals(channel)) return;
		List<Object> dataList = new ArrayList<>(Arrays.asList(data));
		dataList.add(0, subChannel);
		this.write(profile, channel, ByteUtil.toByteArray(dataList));
	}

	// TODO: This needs to support BungeeCord channels
	private void write(BukkitMojangProfile profile, String channel, byte[] data) {
		boolean usePluginMessage = profile.getOfflinePlayer().isOnline();

		/*if (isBungeeSocketListening()) {
			usePluginMessage = false;

			try (Socket socket = new Socket(BUNGEE_SOCKET_IP, BUNGEE_SOCKET_PORT)) {
				try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
					// TODO: This may need to be checked
					dataOutputStream.writeUTF(channel);
					dataOutputStream.writeInt(data.length);
					dataOutputStream.write(data);
				}
			} catch (IOException ioex) {
				this.getLog().console(ioex);
				// TODO: Possibly Disable Socket
				usePluginMessage = true;
			}
		}*/

		if (usePluginMessage)
			profile.getOfflinePlayer().getPlayer().sendPluginMessage(this.getPlugin(), channel, data);
	}

	private class BungeeBukkitListener extends BukkitListener {

		public BungeeBukkitListener() {
			super(NiftyBukkit.getPlugin());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerQuit(PlayerQuitEvent event) {
			BukkitMojangProfile profile = null;

			if (NiftyBukkit.getBungeeHelper().isDetected()) {
				for (BukkitMojangProfile left : NiftyBukkit.getBungeeHelper().getServer().getTotalPlayerList()) {
					if (left.belongsTo(event.getPlayer())) {
						profile = left;
						break;
					}
				}

				NiftyBukkit.getBungeeHelper().getServer().getUnsafePlayerList().remove(profile);
				NiftyBukkit.getBungeeHelper().getServer().playersLeft.remove(profile);

				if (NiftyBukkit.getBungeeHelper().getServer().getPlayerCount() == 0) {
					this.getPlugin().getServer().getPluginManager().callEvent(new BungeeProfileLeaveEvent(profile));
					BUNGEE_ONLINEMODE = false;
					BUNGEE_DETECTED = false;
					SERVERS.clear();
				}
			} else
				profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());

			this.getPlugin().getServer().getPluginManager().callEvent(new ProfileQuitEvent(event, profile));
		}

		@EventHandler
		public void onPluginDisable(PluginDisableEvent event) {
			if (this.getPlugin().equals(event.getPlugin())) {
				/*try {
					getLog().console("Closing socket?");
					BUKKIT_SOCKET.close();
					getLog().console("Socket closed?");
				} catch (Exception ex) {
					getLog().console("Unable to close socket", ex);
				}*/

				for (ChannelWrapper wrapper : LISTENERS)
					wrapper.unregister();
			}
		}

		@EventHandler
		public void onServerListPing(final ServerListPingEvent event) {
			if (!SOCKET_TRIED_ONCE && getSocketWrapper().isSocketListening()) {
				SOCKET_TRIED_ONCE = true;

				try {
					BukkitServerPingEvent pingEvent = new BukkitServerPingEvent(event);
					pingEvent.sendSpoofedVersion(StringUtil.format("{0} {1,number,#}", "NiftyPing", BUKKIT_SOCKET_WRAPPER.getLocalPort()), true);

					MinecraftScheduler.runAsync(new Runnable() {
						@Override
						public void run() {
							try (Socket socket = BUKKIT_SOCKET_WRAPPER.accept()) {
								try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
									if ("NiftyPing".equals(dataInputStream.readUTF())) {
										BUNGEE_SOCKET_IP = dataInputStream.readUTF();
										BUNGEE_SOCKET_PORT = dataInputStream.readInt();
										getLog().console("Loaded Bungee Socket Info: /{0}:{1}", BUNGEE_SOCKET_IP, BUNGEE_SOCKET_PORT);
										MinecraftScheduler.runAsync(new SocketRunnable());
									}

									try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
										dataOutputStream.writeUTF(BUKKIT_SOCKET_WRAPPER.getInetAddress().getHostAddress());
										dataOutputStream.writeInt(BUKKIT_SOCKET_WRAPPER.getLocalPort());
									}
								}
							} catch (IOException ioex) {
								getLog().console("Unable to accept socket connection!", ioex);
								BUNGEE_SOCKET_IP = null;
								BUNGEE_SOCKET_PORT = -1;
							}
						}
					});
				} catch (Exception ex) {
					this.getLog().console("Unable to setup socket channel!");
				}
			}
		}

	}

	public class NiftyChannelWrapper extends ChannelWrapper {

		public NiftyChannelWrapper(JavaPlugin plugin) {
			super(plugin, NIFTY_CHANNEL, null);
		}

		@Override
		protected void handle(String channel, byte[] message) {
			if (!this.isRegistered()) return;
			ByteArrayDataInput input = ByteStreams.newDataInput(message);
			String subChannel = input.readUTF();
			PluginManager manager = this.getPlugin().getServer().getPluginManager();

			if (NIFTY_CHANNEL.equals(channel)) {
				try {
					switch (subChannel) {
						case "BungeeInfo":
							BUNGEE_ONLINEMODE = input.readBoolean();
							break;
						case "GetServers":
							BUNGEE_DETECTED = false;
							SERVERS.clear();
							JsonParser parser = new JsonParser();
							int serverCount = input.readInt();

							for (int i = 0; i < serverCount; i++) {
								JsonObject json = parser.parse(input.readUTF()).getAsJsonObject();
								final BungeeServer server = new BungeeServer(json.get("name").getAsString());
								server.setAddress(json.get("ip").getAsString(), json.get("port").getAsInt());
								SERVERS.put(server.getName(), server);
							}

							BUNGEE_DETECTED = true;
							break;
						default:
							if (!BungeeHelper.this.isDetected()) return;
							final BungeeServer server = BungeeHelper.this.getServer(input.readUTF());

							if ("ServerInfo".equals(subChannel)) {
								server.setOnline(input.readBoolean());

								if (server.isOnline()) {
									server.setMotd(input.readUTF());
									server.setVersion(input.readUTF(), input.readInt());
									server.setMaxPlayers(input.readInt());
									server.getUnsafePlayerList().clear();
									int playerCount = input.readInt();

									for (int i = 0; i < playerCount; i++)
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
								// TODO: PlayerJoin is sent twice for some reason
								System.out.println(subChannel + " CALLED: " + server.getName() + ":" + profile.getName());

								if (subChannel.endsWith("Join")) {
									server.getUnsafePlayerList().add(profile);
									manager.callEvent(new BungeeProfileJoinEvent(profile));
								} else if (subChannel.endsWith("Leave")) {
									if (server.isCurrentServer())
										server.playersLeft.add(profile);

									server.getUnsafePlayerList().remove(profile);
									manager.callEvent(new BungeeProfileLeaveEvent(profile));
								}
							}
							break;
					}
				} catch (Exception ex) {
					if (!(ex instanceof IllegalStateException))
						NiftyBukkit.getPlugin().getLog().console(ex);
				}
			} else if (BUNGEE_CHANNEL.equals(channel)) {
				if ("PlayerUpdate".equals(subChannel)) {
					JsonObject json = new JsonParser().parse(input.readUTF()).getAsJsonObject();
					BukkitMojangProfile updatedProfile = GSON.fromJson(json.toString(), BukkitMojangProfile.class);
					BungeeServer server = SERVERS.get(input.readUTF());

					for (BukkitMojangProfile profile : server.getPlayerList()) {
						if (profile.equals(updatedProfile)) {
							server.getUnsafePlayerList().remove(profile);
							server.getUnsafePlayerList().add(updatedProfile);
							manager.callEvent(new ProfileNameChangeEvent(updatedProfile));
							break;
						}
					}
				}
			}
		}

	}

	private static class SocketRunnable implements Runnable {

		@Override
		public void run() {
			while (getSocketWrapper().isSocketListening()) {
				try (Socket socket = BUKKIT_SOCKET_WRAPPER.accept()) {
					try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
						System.out.println("Reading Stream...");
						String channel = dataInputStream.readUTF();
						int length = dataInputStream.readInt();
						byte[] data = new byte[length];
						dataInputStream.read(data);
						NiftyBukkit.getPlugin().getLog().console("Stream Data: {0} ({1} Bytes)", channel, length);
						NiftyBukkit.getBungeeHelper().NIFTY_WRAPPER.handle(channel, data);
					}
				} catch (IOException ioex) {
					NiftyBukkit.getPlugin().getLog().console(ioex);
					// TODO: Possibly Disable Socket
				}
			}
		}

	}

}