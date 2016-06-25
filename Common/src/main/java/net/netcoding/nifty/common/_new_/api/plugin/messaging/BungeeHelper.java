package net.netcoding.nifty.common._new_.api.plugin.messaging;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.BukkitListener;
import net.netcoding.nifty.common._new_.api.Event;
import net.netcoding.nifty.common._new_.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common._new_.api.plugin.PluginManager;
import net.netcoding.nifty.common._new_.api.plugin.messaging.exceptions.BungeeListenerException;
import net.netcoding.nifty.common._new_.api.plugin.messaging.exceptions.IllegalServerNameException;
import net.netcoding.nifty.common._new_.api.plugin.messaging.exceptions.PluginMessageException;
import net.netcoding.nifty.common._new_.minecraft.event.bungee.BungeeLoadedEvent;
import net.netcoding.nifty.common._new_.minecraft.event.bungee.BungeeProfileJoinEvent;
import net.netcoding.nifty.common._new_.minecraft.event.bungee.BungeeProfileLeaveEvent;
import net.netcoding.nifty.common._new_.minecraft.event.bungee.BungeeServerLoadedEvent;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerNameChangeEvent;
import net.netcoding.nifty.common._new_.minecraft.event.player.PlayerQuitEvent;
import net.netcoding.nifty.common._new_.minecraft.event.server.BukkitServerPingEvent;
import net.netcoding.nifty.common._new_.minecraft.event.server.GameStoppingEvent;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.core.api.MinecraftServer;
import net.netcoding.nifty.core.util.ByteUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.misc.ServerSocketWrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BungeeHelper<T extends BukkitMojangProfile> {

	public static final String BUNGEE_CHANNEL = "BungeeCord";
	static final String NIFTY_CHANNEL = "NiftyBungee";
	private static final transient Gson GSON = new Gson();
	private final ChannelWrapper niftyWrapper;
	private final ConcurrentSet<ChannelWrapper> listeners = new ConcurrentSet<>();
	private final ConcurrentMap<String, BungeeServer<T>> servers = new ConcurrentMap<>();
	private final ServerSocketWrapper bukkitSocketWrapper;
	private final BungeeDetails bungeeDetails = new BungeeDetails();
	private boolean loadedAllOnce = false;

	protected BungeeHelper(ChannelWrapper niftyChannel) {
		if (Nifty.getBungeeHelper() != null)
			throw new UnsupportedOperationException("Only implementations of NiftyBukkit can extend this class!");

		ServerSocket socket = null;

		/*try {
			socket = new ServerSocket(0);
			socket.setSoTimeout(2000);
		} catch (IOException ioex) {
			NiftyBukkit.getPlugin().getLog().console("Unable to register socket!", ioex);
		}*/

		this.bukkitSocketWrapper = new ServerSocketWrapper(socket);
		this.niftyWrapper = niftyChannel;
		this.niftyWrapper.register();
		this.listeners.add(niftyWrapper);
	}

	public final void connect(BukkitMojangProfile profile, String targetServer) {
		this.write(this.getFirstProfile(), BUNGEE_CHANNEL, "ConnectOther", profile.getName(), targetServer);
	}

	public final void forward(String subChannel, Object... data) {
		this.forward("ALL", subChannel, data);
	}

	public final void forward(String targetServer, String subChannel, Object... data) {
		this.forward(this.getFirstProfile(), targetServer, subChannel, data);
	}

	public final void forward(BukkitMojangProfile profile, String subChannel, Object... data) {
		this.forward(profile, "ALL", subChannel, data);
	}

	public final void forward(BukkitMojangProfile profile, String targetServer, String subChannel, Object... data) {
		if (!this.getDetails().isDetected()) throw new BungeeListenerException();
		if (StringUtil.isEmpty(targetServer)) throw new PluginMessageException("Target server cannot be null!");
		if (StringUtil.isEmpty(subChannel)) throw new PluginMessageException("Sub channel cannot be null!");
		if (subChannel.equalsIgnoreCase(NIFTY_CHANNEL)) throw new PluginMessageException(StringUtil.format("You cannot forward to {0} channels!", NIFTY_CHANNEL));
		if (subChannel.matches("^GetServers?|Player(?:Count|List)|UUID(?:Other)?|(?:Server)?IP$")) throw new PluginMessageException(StringUtil.format("The {{0}} {1} channels are handled automatically; manual forwarding disabled!", subChannel, BUNGEE_CHANNEL));
		byte[] forward = ByteUtil.toByteArray(data);
		this.write(profile, BUNGEE_CHANNEL, ByteUtil.toByteArray("Forward", targetServer, subChannel, (short)forward.length, forward));
	}

	public final BungeeDetails getDetails() {
		return this.bungeeDetails;
	}

	private T getFirstProfile() {
		return this.getServer().getPlayerCount() > 0 ? this.getServer().getPlayerList().iterator().next() : null;
	}

	public final int getMaxPlayers() {
		return this.getDetails().isDetected() ? this.getMaxPlayers(this.getServerName()) : Nifty.getServer().getMaxPlayers();
	}

	public final int getMaxPlayers(String serverName) throws IllegalServerNameException, BungeeListenerException {
		if (this.getDetails().isDetected()) {
			if ("ALL".equalsIgnoreCase(serverName)) {
				int maxCount = 0;

				for (BungeeServer<T> server : this.servers.values())
					maxCount += server.getMaxPlayers();

				return maxCount;
			}

			BungeeServer<T> server = this.servers.get(serverName);

			if (server != null)
				return server.getMaxPlayers();

			throw new IllegalServerNameException(serverName);
		}

		throw new BungeeListenerException();
	}

	public final int getPlayerCount() {
		return this.getDetails().isDetected() ? this.getPlayerCount(this.getServerName()) : Nifty.getServer().getPlayerCount();
	}

	public final int getPlayerCount(String serverName) throws IllegalServerNameException, BungeeListenerException {
		if (this.getDetails().isDetected()) {
			if ("ALL".equalsIgnoreCase(serverName)) {
				int playerCount = 0;

				for (BungeeServer<T> server : this.servers.values())
					playerCount += server.getPlayerCount();

				return playerCount;
			}

			BungeeServer<T> server = this.servers.get(serverName);

			if (server != null)
				return server.getPlayerCount();

			throw new IllegalServerNameException(serverName);
		}

		throw new BungeeListenerException();
	}

	public final Collection<T> getPlayerList() {
		if (this.getDetails().isDetected())
			return this.getPlayerList(this.getServerName());

		T[] profiles = Nifty.<T>getMojangRepository().searchByPlayer(Nifty.getServer().getPlayerList());
		return Collections.unmodifiableCollection(Arrays.asList(profiles));
	}

	public final Collection<T> getPlayerList(String serverName) throws IllegalServerNameException, BungeeListenerException {
		if (this.getDetails().isDetected()) {
			if ("ALL".equalsIgnoreCase(serverName)) {
				Set<T> playerNames = new HashSet<>();

				for (BungeeServer<T> server : this.servers.values())
					playerNames.addAll(server.getPlayerList());

				return Collections.unmodifiableCollection(playerNames);
			}

			BungeeServer<T> server = this.servers.get(serverName);

			if (server != null)
				return server.getPlayerList();

			throw new IllegalServerNameException(serverName);
		} else if ("ALL".equalsIgnoreCase(serverName))
			return this.getPlayerList();

		throw new BungeeListenerException();
	}

	public final BungeeServer<T> getPlayerServer(BukkitMojangProfile profile) throws PluginMessageException, BungeeListenerException {
		if (this.getDetails().isDetected()) {
			for (BungeeServer<T> server : this.getServers()) {
				if (server.getPlayerList().contains(profile))
					return server;
			}

			throw new PluginMessageException(StringUtil.format("Unable to locate the server of {0}!", profile));
		}

		throw new BungeeListenerException();
	}

	public final BungeeServer<T> getServer() throws BungeeListenerException {
		if (this.getDetails().isDetected()) {
			BungeeServer<T> currentServer = null;

			for (BungeeServer<T> server : this.servers.values()) {
				if (server.isCurrentServer()) {
					currentServer = server;
					break;
				}
			}

			return currentServer;
		}

		throw new BungeeListenerException();
	}

	public final BungeeServer<T> getServer(String serverName) throws BungeeListenerException {
		if (this.getDetails().isDetected())
			return this.servers.get(serverName);

		throw new BungeeListenerException();
	}

	public final String getServerName() throws BungeeListenerException {
		for (BungeeServer<T> server : this.servers.values()) {
			if (server.isCurrentServer())
				return server.getName();
		}

		throw new BungeeListenerException();
	}

	public final Set<String> getServerNames() throws BungeeListenerException {
		if (this.getDetails().isDetected())
			return Collections.unmodifiableSet(this.servers.values().stream().map(MinecraftServer::getName).collect(Collectors.toSet()));

		throw new BungeeListenerException();
	}

	public final Set<BungeeServer<T>> getServers() throws BungeeListenerException {
		if (this.getDetails().isDetected())
			return Collections.unmodifiableSet(new ConcurrentSet<>(this.servers.values()));

		throw new BungeeListenerException();
	}

	public final ServerSocketWrapper getSocketWrapper() {
		return bukkitSocketWrapper;
	}

	@SuppressWarnings("unchecked")
	protected final Class<T> getSuperClass() {
		ParameterizedType superClass = (ParameterizedType)this.getClass().getGenericSuperclass();
		return (Class<T>)(superClass.getActualTypeArguments().length == 0 ? BukkitMojangProfile.class : superClass.getActualTypeArguments()[0]);
	}

	void handleNifty(String channel, byte[] message) {
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();
		PluginManager manager = Nifty.getServer().getPluginManager();

		if (NIFTY_CHANNEL.equals(channel)) {
			try {
				switch (subChannel) {
					case "BungeeInfo":
						this.bungeeDetails.setOnlineMode(input.readBoolean());
						break;
					case "GetServers":
						this.bungeeDetails.setDetected(false);
						this.servers.clear();
						JsonParser parser = new JsonParser();
						int serverCount = input.readInt();

						for (int i = 0; i < serverCount; i++) {
							JsonObject json = parser.parse(input.readUTF()).getAsJsonObject();
							final BungeeServer<T> server = new BungeeServer<>(json.get("name").getAsString());
							server.setAddress(json.get("ip").getAsString(), json.get("port").getAsInt());
							this.servers.put(server.getName(), server);
						}

						this.bungeeDetails.setDetected(true);
						break;
					default:
						if (!this.getDetails().isDetected()) return;
						final BungeeServer<T> server = this.getServer(input.readUTF());

						if ("ServerInfo".equals(subChannel)) {
							server.setOnline(input.readBoolean());

							if (server.isOnlineMode()) {
								server.setMotd(input.readUTF());
								server.setVersion(input.readUTF(), input.readInt());
								server.setMaxPlayers(input.readInt());
								server.getUnsafePlayerList().clear();
								int playerCount = input.readInt();

								for (int i = 0; i < playerCount; i++)
									server.getUnsafePlayerList().add(GSON.fromJson(input.readUTF(), this.getSuperClass()));
							} else
								server.reset();

							server.loadedOnce = true;
							manager.call(new BungeeServerLoadedEvent(server));

							if (!loadedAllOnce) {
								int loaded = 0;

								for (BungeeServer<T> serv : this.servers.values())
									loaded += serv.loadedOnce ? 1 : 0;

								if (loaded == this.servers.size()) {
									loadedAllOnce = true;
									manager.call(new BungeeLoadedEvent());
								}
							}
						} else if (subChannel.startsWith("Player")) {
							T profile = GSON.fromJson(input.readUTF(), this.getSuperClass());
							// TODO: PlayerJoin is sent twice for some reason
							System.out.println(subChannel + " CALLED: " + server.getName() + ":" + profile.getName());

							if (subChannel.endsWith("Join")) {
								server.getUnsafePlayerList().add(profile);
								manager.call(new BungeeProfileJoinEvent(profile));
							} else if (subChannel.endsWith("Leave")) {
								if (server.isCurrentServer())
									server.playersLeft.add(profile);

								server.getUnsafePlayerList().remove(profile);
								manager.call(new BungeeProfileLeaveEvent(profile));
							}
						}
						break;
				}
			} catch (Exception ex) {
				if (!(ex instanceof IllegalStateException))
					Nifty.getPlugin().getLog().console(ex);
			}
		} else {
			if (BUNGEE_CHANNEL.equals(channel)) {
				if ("PlayerUpdate".equals(subChannel)) {
					JsonObject json = new JsonParser().parse(input.readUTF()).getAsJsonObject();
					T updatedProfile = GSON.fromJson(json.toString(), this.getSuperClass());
					BungeeServer<T> server = this.servers.get(input.readUTF());

					for (T profile : server.getPlayerList()) {
						if (profile.equals(updatedProfile)) {
							server.getUnsafePlayerList().remove(profile);
							server.getUnsafePlayerList().add(updatedProfile);
							manager.call(new PlayerNameChangeEvent(updatedProfile));
							break;
						}
					}
				}
			}

			Nifty.getMessenger().dispatch(channel, message);
		}
	}

	public final boolean isPlayerOnline(BukkitMojangProfile profile) {
		if (profile.getOfflinePlayer().isOnline())
			return true;
		else if (this.getDetails().isDetected()) {
			for (BungeeServer<T> server : this.getServers()) {
				if (server.getPlayerList().contains(profile))
					return true;
			}
		}

		return false;
	}

	public final void message(BukkitMojangProfile toProfile, String message) {
		this.message(this.getFirstProfile(), toProfile, message);
	}

	public final void message(BukkitMojangProfile fromProfile, BukkitMojangProfile profile, String message) {
		this.write(fromProfile, BUNGEE_CHANNEL, "Message", profile.getName(), message);
	}

	/**
	 * Registers a plugin message listener.
	 *
	 * @param plugin Plugin the listener is bound to.
	 * @param listener Channel listener implementation to send results to.
	 * @return Wrapped channel containing plugin, BungeeCord channel and listener.
	 */
	public final ChannelWrapper register(MinecraftPlugin plugin, ChannelListener listener) {
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
	public final ChannelWrapper register(MinecraftPlugin plugin, String channel, ChannelListener listener) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		Preconditions.checkArgument(StringUtil.notEmpty(channel), "Channel cannot be NULL!");
		Preconditions.checkArgument(listener != null, "Listener cannot be NULL!");

		if (!this.getDetails().isDetected())
			throw new PluginMessageException(StringUtil.format("You cannot register a listener until {0} has been found!", BUNGEE_CHANNEL));

		if (NIFTY_CHANNEL.equalsIgnoreCase(channel))
			throw new PluginMessageException(StringUtil.format("You cannot register a listener using the ''{0}'' channel!", NIFTY_CHANNEL));

		ChannelWrapper wrapper = new ChannelWrapper(plugin, channel, listener);

		if (this.listeners.contains(wrapper))
			throw new PluginMessageException(StringUtil.format("The channel ''{0}'' is already registered for plugin ''{1}''!", channel, plugin.getName()));

		wrapper.register();
		this.listeners.add(wrapper);
		return wrapper;
	}

	protected abstract void sendPluginMessage(MinecraftPlugin plugin, BukkitMojangProfile profile, String channel, byte[] data);

	void write(BukkitMojangProfile profile, String channel, String subChannel, Object... data) {
		if (!this.getDetails().isDetected()) return;
		if (StringUtil.isEmpty(subChannel)) throw new PluginMessageException("Sub channel cannot be NULL!");
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

			try (Socket socket = new Socket(bungeeSocketIp, bungeeSocketPort)) {
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
			this.sendPluginMessage(Nifty.getPlugin(), profile, channel, data);
	}

	// TODO: Custom Listener
	private class BungeeBukkitListener extends BukkitListener {

		public BungeeBukkitListener() {
			super(Nifty.getPlugin());
		}

		@Event(priority = Event.Priority.MONITOR)
		public void onPlayerQuit(PlayerQuitEvent event) {
			if (BungeeHelper.this.getDetails().isDetected()) {
				BukkitMojangProfile profile = null;

				for (BukkitMojangProfile left : BungeeHelper.this.getServer().getTotalPlayerList()) {
					if (left.equals(event.getProfile())) {
						profile = left;
						break;
					}
				}

				if (profile != null) {
					BungeeHelper.this.getServer().getUnsafePlayerList().remove(profile);
					BungeeHelper.this.getServer().playersLeft.remove(profile);

					if (BungeeHelper.this.getServer().getPlayerCount() == 0) {
						Nifty.getServer().getPluginManager().call(new BungeeProfileLeaveEvent(profile));
						BungeeHelper.this.bungeeDetails.setDetected(false);
						BungeeHelper.this.bungeeDetails.setOnlineMode(false);
						BungeeHelper.this.servers.clear();
					}
				}
			}
		}

		@Event
		public void onGameStopping(GameStoppingEvent event) {
			/*try {
				getLog().console("Closing socket?");
				BUKKIT_SOCKET.close();
				getLog().console("Socket closed?");
			} catch (Exception ex) {
				getLog().console("Unable to close socket", ex);
			}*/

			BungeeHelper.this.listeners.forEach(ChannelWrapper::unregister);
		}

		@Event
		public void onBukkitServerPing(final BukkitServerPingEvent event) {
			BungeeDetails details = BungeeHelper.this.getDetails();

			if (!details.isSocketTriedOnce() && getSocketWrapper().isSocketListening()) {
				details.setSocketTriedOnce();

				try {
					event.sendSpoofedVersion(StringUtil.format("{0} {1,number,#}", "NiftyPing", bukkitSocketWrapper.getLocalPort()), true);

					Nifty.getScheduler().runAsync(() -> {
						try (Socket socket = bukkitSocketWrapper.accept()) {
							try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
								if ("NiftyPing".equals(dataInputStream.readUTF())) {
									details.setSocketIp(dataInputStream.readUTF());
									details.setSocketPort(dataInputStream.readInt());
									getLog().console("Loaded Bungee Socket Info: /{0}:{1}", details.getSocketIp(), details.getSocketPort());
									Nifty.getScheduler().runAsync(new SocketRunnable());
								}

								try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
									dataOutputStream.writeUTF(bukkitSocketWrapper.getInetAddress().getHostAddress());
									dataOutputStream.writeInt(bukkitSocketWrapper.getLocalPort());
								}
							}
						} catch (IOException ioex) {
							getLog().console("Unable to accept socket connection!", ioex);
							details.setSocketIp("");
							details.setSocketPort(-1);
						}
					});
				} catch (Exception ex) {
					this.getLog().console("Unable to setup socket channel!");
				}
			}
		}

	}

	private class SocketRunnable implements Runnable {

		@SuppressWarnings("ResultOfMethodCallIgnored")
		@Override
		public void run() {
			while (getSocketWrapper().isSocketListening()) {
				try (Socket socket = bukkitSocketWrapper.accept()) {
					try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
						System.out.println("Reading Stream...");
						String channel = dataInputStream.readUTF();
						int length = dataInputStream.readInt();
						byte[] data = new byte[length];
						dataInputStream.read(data);
						Nifty.getPlugin().getLog().console("Stream Data: {0} ({1} Bytes)", channel, length);
						BungeeHelper.this.niftyWrapper.handle(channel, data);
					}
				} catch (IOException ioex) {
					Nifty.getPlugin().getLog().console(ioex);
					// TODO: Possibly Disable Socket
				}
			}
		}

	}

}