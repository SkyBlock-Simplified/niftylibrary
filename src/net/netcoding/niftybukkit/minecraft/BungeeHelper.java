package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
	private final String channel;
	private static final ConcurrentHashMap<String, BungeeServer> serverList = new ConcurrentHashMap<>();
	private final transient BungeeListener listener;

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

	public BungeeHelper(JavaPlugin plugin, String channel, BungeeListener listener) {
		this(plugin, BUNGEE_CHANNEL, listener, false);
	}

	public BungeeHelper(JavaPlugin plugin, String channel, boolean register) {
		this(plugin, channel, null, register);
	}

	public BungeeHelper(JavaPlugin plugin, String channel, BungeeListener listener, boolean register) {
		super(plugin);
		if (StringUtil.isEmpty(channel)) throw new IllegalArgumentException("A channel name must be passed when instantiating an instance of BungeeHelper!");

		if (loadedOnce) {
			if (!this.isOnline())
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class until {0} has been found!", BUNGEE_CHANNEL));
			else if (channel.equals(NIFTY_CHANNEL))
				throw new UnsupportedOperationException(StringUtil.format("You cannot instantiate an instance of this class using the channel {0}!", NIFTY_CHANNEL));
		} else
			loadedOnce = true;

		this.channel = channel;
		this.listener = listener;
		if (register) this.register();
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
		if (!this.isOnline()) throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
		if (player == null) throw new IllegalArgumentException("Player cannot be null!");
		if (subChannel.equalsIgnoreCase(NIFTY_CHANNEL)) throw new IllegalArgumentException("You cannot forward to NiftyBungee channels!");
		if (subChannel.matches("^GetServers?|Player(?:Count|List)|UUID(?:Other)?$")) throw new IllegalArgumentException(String.format("The GetServer, GetServers, PlayerCount and PlayerList %s channels are handled automatically; manual forwarding disabled!", BUNGEE_CHANNEL));
		byte[] forward = ByteUtil.toByteArray(data);
		byte[] output = ByteUtil.toByteArray("Forward", targetServer, subChannel, (short)forward.length, forward);
		player.sendPluginMessage(this.getPlugin(), BUNGEE_CHANNEL, output);
	}

	public String getChannel() {
		return this.channel;
	}

	private Player getFirstPlayer() {
		return this.getPlugin().getServer().getOnlinePlayers().length > 0 ? this.getPlugin().getServer().getOnlinePlayers()[0] : null;
	}

	public int getPlayerCount() {
		if (this.isOnline())
			return this.getPlayerCount(this.getServerName());
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
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
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public void getPlayerIP(Player player) {
		this.write(player, BUNGEE_CHANNEL, "IP");
	}

	public BungeeServer getPlayerServer(MojangProfile profile) {
		if (this.isOnline()) {
			for (BungeeServer server : this.getServers()) {
				if (server.getPlayerList().contains(profile))
					return server;
			}

			throw new RuntimeException(StringUtil.format("Unable to locate the server of {0}!", profile.getName()));
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public Set<MojangProfile> getPlayerList() {
		if (this.isOnline())
			return this.getPlayerList(this.getServerName());
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
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
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getServer() {
		if (this.isOnline()) {
			BungeeServer currentServer = null;

			for (BungeeServer server : serverList.values()) {
				if (server.isCurrentServer()) {
					currentServer = server;
					break;
				}
			}

			return currentServer;
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public BungeeServer getServer(String serverName) {
		if (this.isOnline())
			return serverList.get(serverName);
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public String getServerName() {
		for (BungeeServer server : serverList.values()) {
			if (server.isCurrentServer())
				return server.getName();
		}

		throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public Set<String> getServerNames() {
		if (this.isOnline()) {
			Set<String> serverNames = new HashSet<>();
			for (BungeeServer server : serverList.values()) serverNames.add(server.getName());
			return Collections.unmodifiableSet(serverNames);
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public Set<BungeeServer> getServers() {
		if (this.isOnline())
			return Collections.unmodifiableSet(new HashSet<>(serverList.values()));
		else
			throw new UnsupportedOperationException(String.format("No %s listener available to query!", BUNGEE_CHANNEL));
	}

	public boolean isRegistered() {
		return this.getPlugin().getServer().getMessenger().isIncomingChannelRegistered(this.getPlugin(), this.getChannel()) && this.getPlugin().getServer().getMessenger().isOutgoingChannelRegistered(this.getPlugin(), this.getChannel());
	}

	public boolean isOnline() {
		return bungeeOnline;
	}

	public boolean isPlayerOnline(MojangProfile profile) {
		if (profile == null) return false;

		for (BungeeServer server : this.getServers()) {
			if (server.getPlayerList().contains(profile))
				return true;
		}

		return false;
	}

	public void message(MojangProfile profile, String message) {
		this.message(this.getFirstPlayer(), profile, message);
	}

	public void message(Player player, MojangProfile profile, String message) {
		this.write(player, BUNGEE_CHANNEL, "Message", profile.getName(), message);
	}

	public void register() {
		if (this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!this.getChannel().equals(BUNGEE_CHANNEL)) {
			this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
			this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);
		}
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!this.getChannel().equals(BUNGEE_CHANNEL)) {
			this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
			this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL);
		}
	}

	public void write(Player player, String subChannel, Object... data) {
		this.write(player, this.getChannel(), subChannel, data);
	}

	private void write(Player player, String channel, String subChannel, Object... data) {
		if (!this.isOnline()) return;
		if (channel.equals("Forward")) return;
		List<Object> dataList = new ArrayList<>(Arrays.asList(data));
		dataList.add(0, subChannel);
		player.sendPluginMessage(this.getPlugin(), channel, ByteUtil.toByteArray(dataList));
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!this.isRegistered()) return;
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();

		if (channel.equals(NIFTY_CHANNEL)) {
			try {
				PluginManager manager = this.getPlugin().getServer().getPluginManager();

				if (subChannel.equals("GetServers")) {
					serverList.clear();
					int count = input.readInt();

					for (int i = 0; i < count; i++) {
						final BungeeServer server = new BungeeServer(input.readUTF());
						server.setAddress(input.readUTF(), input.readInt());
						serverList.put(server.getName(), server);
						manager.callEvent(new BungeeServerLoadedEvent(server));
					}

					bungeeOnline = true;
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

							for (MojangProfile tempPlayer : server.tempList)
								server.playerList.add(tempPlayer);

							server.tempList.clear();
						} else
							server.reset();

						server.loadedOnce = true;
						manager.callEvent(new BungeeServerLoadedEvent(server));

						if (!bungeeLoaded) {
							int loaded = 0;

							for (BungeeServer serv : serverList.values())
								loaded += serv.loadedOnce ? 1 : 0;

							if (loaded == serverList.size()) {
								bungeeLoaded = true;
								manager.callEvent(new BungeeLoadedEvent(serverList.values()));
							}
						}

					} else if (subChannel.equals("ServerOffline"))
						server.reset();
					else if (subChannel.startsWith("Player")) {
						String playerName = input.readUTF();

						if (subChannel.endsWith("Join")) {
							MojangProfile profile = new MojangProfile(playerName, input.readUTF());
							server.playerList.add(profile);
							server.tempList.add(profile);
							manager.callEvent(new BungeePlayerJoinEvent(server, profile));
						} else if (subChannel.endsWith("Leave")) {
							UUID uniqueId = UUID.fromString(playerName);

							for (MojangProfile profile : server.playerList) {
								if (profile.getUniqueId().equals(uniqueId)) {
									server.playerList.remove(profile);
									server.tempList.remove(profile);
									manager.callEvent(new BungeePlayerLeaveEvent(server, profile));
									break;
								}
							}
						}
					}
				}
			} catch (IllegalStateException ise) { } catch (Exception ex) {
				this.getLog().console(ex);
			}
		} else {
			if (channel.equals(BUNGEE_CHANNEL)) {
				if (subChannel.matches("^Player(?:Count|List)|GetServers?|UUID(?:Other)?$"))
					return;
			}

			if (channel.equals(this.getChannel())) {
				if (this.listener != null) {
					try {
						this.listener.onMessageReceived(channel, player, message);
					} catch (IllegalStateException eof) { } catch (Exception ex) {
						this.getLog().console(ex);
					}
				}
			}
		}
	}

}