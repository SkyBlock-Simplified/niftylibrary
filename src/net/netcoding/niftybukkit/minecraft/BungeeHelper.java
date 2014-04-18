package net.netcoding.niftybukkit.minecraft;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.minecraft.events.BungeeLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerJoinEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeePlayerLeaveEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeServerLoadedEvent;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.ByteUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class BungeeHelper extends BukkitHelper implements PluginMessageListener {

	public static final String BUNGEE_CHANNEL = "BungeeCord";
	public static final String NIFTY_CHANNEL = "NiftyBungee";
	private static boolean bungeeOnline = false;
	private static boolean bungeeLoaded = false;
	private static boolean loadedOnce = false;
	private static final ConcurrentHashMap<String, BungeeServer> serverList = new ConcurrentHashMap<>();
	private transient BungeeListener listener;

	public BungeeHelper(JavaPlugin plugin) {
		this(plugin, null);
	}

	public BungeeHelper(JavaPlugin plugin, BungeeListener listener) {
		this(plugin, listener, false);
	}

	public BungeeHelper(JavaPlugin plugin, BungeeListener listener, boolean register) {
		super(plugin);

		if (loadedOnce) {
			if (!this.isOnline())
				throw new UnsupportedOperationException(String.format("You cannot instantiate an instance of this class until %s has been found!", BUNGEE_CHANNEL));
		} else
			loadedOnce = true;

		this.listener = listener;
		if (this.listener != null && register) this.register();
	}

	public static boolean bungeeOnline() {
		return bungeeOnline;
	}

	public void connect(Player player, String targetServer) {
		this.write(player, "Connect", targetServer);
	}

	public void connect(String targetPlayer, String targetServer) {
		this.write(this.getFirstPlayer(), "ConnectOther", targetPlayer, targetServer);
	}

	public void forward(String subChannel, Object... data) {
		this.forward("ALL", subChannel, data);
	}

	public void forward(String targetServer, String subChannel, Object... data) {
		this.forward(this.getFirstPlayer(), targetServer, subChannel, data);
	}

	public void forward(Player player, String subChannel, Object... data) {
		this.forward(player, "ALL", subChannel, data);
	}

	public void forward(Player player, String targetServer, String subChannel, Object... data) {
		if (subChannel.equalsIgnoreCase(NIFTY_CHANNEL)) throw new IllegalArgumentException("You cannot forward to Nifty channels!");
		if (subChannel.matches("^GetServers?|Player(?:Count|List)$")) throw new IllegalArgumentException(String.format("The GetServer, GetServers, PlayerCount and PlayerList %s channels are handled automatically; manual forwarding disabled!", BUNGEE_CHANNEL));
		forward(this.getPlugin(), player, targetServer, subChannel, data);
	}

	private static void forward(JavaPlugin plugin, Player player, String targetServer, String subChannel, Object... data) {
		if (!bungeeOnline()) return;
		if (player == null) return;
		byte[] forward = ByteUtil.toByteArray(data);
		byte[] output = ByteUtil.toByteArray("Forward", targetServer, subChannel, forward.length, forward);
		player.sendPluginMessage(plugin, BUNGEE_CHANNEL, output);
	}

	private Player getFirstPlayer() {
		return this.getPlugin().getServer().getOnlinePlayers().length > 0 ? this.getPlugin().getServer().getOnlinePlayers()[0] : null;
	}

	public int getPlayerCount() {
		if (this.isOnline())
			return this.getPlayerCount(this.getServerName());
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public int getPlayerCount(String serverName) {
		if (this.isOnline()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				int playerCount = 0;

				for (BungeeServer server : serverList.values())
					playerCount += server.getPlayerCount();

				return playerCount;
			} else
				return serverList.get(serverName).getPlayerCount();
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public void getPlayerIP(Player player) {
		this.write(player, "IP");
	}

	public Set<MojangProfile> getPlayerList() {
		if (this.isOnline())
			return this.getPlayerList(this.getServerName());
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public Set<MojangProfile> getPlayerList(String serverName) {
		if (this.isOnline()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				Set<MojangProfile> playerNames = new HashSet<>();
				for (BungeeServer server : serverList.values()) playerNames.addAll(server.getPlayerList());
				return Collections.unmodifiableSet(playerNames);
			} else
				return serverList.get(serverName).getPlayerList();
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public BungeeServer getServer() {
		BungeeServer currentServer = null;

		for (BungeeServer server : serverList.values()) {
			if (server.isCurrentServer()) {
				currentServer = server;
				break;
			}
		}

		if (currentServer == null)
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
		else
			return currentServer;
	}

	public BungeeServer getServer(String serverName) {
		return serverList.get(serverName);
	}

	public String getServerName() {
		for (BungeeServer server : serverList.values()) {
			if (server.isCurrentServer())
				return server.getName();
		}

		throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public Set<String> getServerNames() {
		if (this.isOnline()) {
			Set<String> serverNames = new HashSet<>();
			for (BungeeServer server : serverList.values()) serverNames.add(server.getName());
			return Collections.unmodifiableSet(serverNames);
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public Set<BungeeServer> getServers() {
		return Collections.unmodifiableSet(new HashSet<>(serverList.values()));
	}

	public boolean isRegistered() {
		return this.getPlugin().getServer().getMessenger().isIncomingChannelRegistered(this.getPlugin(), BUNGEE_CHANNEL) && this.getPlugin().getServer().getMessenger().isOutgoingChannelRegistered(this.getPlugin(), BUNGEE_CHANNEL);
	}

	public boolean isOnline() {
		return bungeeOnline;
	}

	public boolean isPlayerOnline(String playerName) {
		if (StringUtil.isEmpty(playerName)) return false;

		for (BungeeServer server : serverList.values()) {
			for (MojangProfile profile : server.playerList) {
				if (profile.getName().equalsIgnoreCase(playerName))
					return true;
			}
		}

		return false;
	}

	public void message(Player player, String targetPlayer, String message) {
		this.write(player, "Message", targetPlayer, message);
	}

	public void register() {
		if (this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
	}

	private void write(Player player, String channel, Object... objs) {
		write(this.getPlugin(), player, channel, objs);
	}

	private static void write(JavaPlugin plugin, Player player, String subChannel, Object... data) {
		write(plugin, player, BUNGEE_CHANNEL, subChannel, data);
	}

	private static void write(JavaPlugin plugin, Player player, String channel, String subChannel, Object... data) {
		if (!bungeeOnline()) return;
		if (channel.equals("Forward")) return;
		List<Object> dataList = new ArrayList<>(Arrays.asList(data));
		dataList.add(0, subChannel);
		player.sendPluginMessage(plugin, channel, ByteUtil.toByteArray(dataList));
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();

		if (channel.equals(BUNGEE_CHANNEL)) {
			if (subChannel.matches("^Player(?:Count|List)|GetServers?$")) return;

			if (this.listener != null) {
				try {
					this.listener.onMessageReceived(subChannel, player, message);
				} catch (Exception ex) {
					this.getLog().console(ex);
				}
			}
		} else if (channel.equals("NiftyBungee")) {
			try {
				PluginManager manager = this.getPlugin().getServer().getPluginManager();

				if (subChannel.equals("GetServers")) {
					bungeeOnline = true;
					serverList.clear();
					int count = input.readInt();

					for (int i = 0; i < count; i++) {
						final BungeeServer server = new BungeeServer(input.readUTF());
						server.setAddress(input.readUTF(), input.readInt());
						serverList.put(server.getName(), server);
						manager.callEvent(new BungeeServerLoadedEvent(server));
					}
				} else {
					final BungeeServer server = this.getServer(input.readUTF());
					if (server == null) return;

					if (subChannel.equals("ServerInfo")) {
						server.setOnline(input.readBoolean());

						if (server.isOnline()) {
							server.setMotd(input.readUTF());
							server.setGameVersion(input.readUTF());
							server.setProtocolVersion(input.readInt());
							server.setMaxPlayers(input.readInt());
							int count = input.readInt();
							server.playerList.clear();

							for (int i = 0; i < count; i += 2)
								server.playerList.add(new MojangProfile(input.readUTF(), input.readUTF()));
						} else
							server.reset();

						if (!server.loadedOnce) {
							server.loadedOnce = true;
							manager.callEvent(new BungeeServerLoadedEvent(server));
						}

						int loaded = 0;

						for (BungeeServer serv : serverList.values())
							loaded += serv.loadedOnce ? 1 : 0;

						if (loaded == serverList.size() && !bungeeLoaded) {
							bungeeLoaded = true;
							manager.callEvent(new BungeeLoadedEvent(serverList.values()));
						}
					} else if (subChannel.startsWith("Player")) {
						String playerName = input.readUTF();

						if (subChannel.endsWith("Join")) {
							MojangProfile profile = new MojangProfile(playerName, input.readUTF());
							server.playerList.add(profile);
							manager.callEvent(new BungeePlayerJoinEvent(server, profile));
						} else if (subChannel.endsWith("Leave")) {
							for (MojangProfile profile : server.playerList) {
								if (profile.getName().equals(playerName)) {
									server.playerList.remove(profile);
									manager.callEvent(new BungeePlayerLeaveEvent(server, profile));
									break;
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				if (!ex.getClass().equals(EOFException.class))
					this.getLog().console(ex);
			}
		}
	}

}