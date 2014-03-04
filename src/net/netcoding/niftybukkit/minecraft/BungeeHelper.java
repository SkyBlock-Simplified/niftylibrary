package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.events.BungeeLoadedEvent;
import net.netcoding.niftybukkit.minecraft.events.BungeeServerLoadedEvent;
import net.netcoding.niftybukkit.utilities.CIHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeHelper extends BukkitHelper implements PluginMessageListener {

	private static transient int LOADED_SERVERS = 0;
	public static final transient String BUNGEE_CHANNEL = "BungeeCord";
	private static transient boolean bungeeOnline = false;
	private static transient boolean loadedOnce = false;
	private static final transient CIHashMap<BukkitServer> serverList = new CIHashMap<>();
	private transient BungeeListener listener;

	static {
		if (!Bukkit.getServer().getOnlineMode())
			new BungeeChecker(NiftyBukkit.getPlugin());
	}

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
		if (subChannel.toLowerCase().startsWith("Nifty".toLowerCase())) throw new IllegalArgumentException("You cannot forward to Nifty channels!");
		if (subChannel.matches("^(?:GetServers?|Player(?:Count|List))$")) throw new IllegalArgumentException(String.format("The GetServer, GetServers, PlayerCount and PlayerList %s channels are handled automatically; manual forwarding disabled!", BUNGEE_CHANNEL));
		forward(this.getPlugin(), player, targetServer, subChannel, data);
	}

	private static void forward(JavaPlugin plugin, Player player, String targetServer, String subChannel, Object... data) {
		if (!bungeeOnline()) return;
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		ByteArrayDataOutput forward = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF(targetServer);
		out.writeUTF(subChannel);

		if (data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				if (data[i] instanceof String)
					forward.writeUTF((String)data[i]);
				else if (data[i] instanceof Short)
					forward.writeShort((short)data[i]);
				else if (data[i] instanceof Integer)
					forward.writeInt((int)data[i]);
				else if (data[i] instanceof Boolean)
					forward.writeBoolean((boolean)data[i]);
			}
		}

		byte[] fdata = forward.toByteArray();
		out.writeShort(data.length);
		out.write(fdata);
		player.sendPluginMessage(plugin, BUNGEE_CHANNEL, out.toByteArray());
	}

	private Player getFirstPlayer() {
		return Bukkit.getOnlinePlayers().length > 0 ? Bukkit.getOnlinePlayers()[0] : null;
	}

	public int getPlayerCount() {
		return this.isOnline() ? this.getPlayerCount(this.getServerName()) : Bukkit.getOnlinePlayers().length;
	}

	public int getPlayerCount(String serverName) {
		if (this.isOnline()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				int playerCount = 0;

				for (BukkitServer server : serverList.values())
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

	public List<String> getPlayerList() {
		if (this.isOnline())
			return this.getPlayerList(this.getServerName());
		else {
			List<String> playerNames = new ArrayList<>();
			for (Player player : Bukkit.getOnlinePlayers()) playerNames.add(player.getName());
			return Collections.unmodifiableList(playerNames);
		}
	}

	public List<String> getPlayerList(String serverName) {
		if (this.isOnline()) {
			if (serverName.equalsIgnoreCase("ALL")) {
				List<String> playerNames = new ArrayList<>();

				for (BukkitServer server : serverList.values())
					playerNames.addAll(server.getPlayerList());

				return Collections.unmodifiableList(playerNames);
			} else
				return serverList.get(serverName).getPlayerList();
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public BukkitServer getServer() {
		BukkitServer currentServer = null;

		for (BukkitServer server : serverList.values()) {
			if (server.isCurrentServer()) {
				currentServer = server;
				break;
			}
		}

		if (currentServer == null)
			throw new UnsupportedOperationException(String.format("No %s servers have been loaded!", BUNGEE_CHANNEL));
		else
			return currentServer;
	}

	public BukkitServer getServer(String serverName) {
		return serverList.get(serverName);
	}

	public String getServerName() {
		String serverName = Bukkit.getServerName();

		for (BukkitServer server : serverList.values()) {
			if (server.isCurrentServer()) {
				serverName = server.getName();
				break;
			}
		}

		return serverName;
	}

	public List<String> getServerNames() {
		if (this.isOnline()) {
			List<String> serverNames = new ArrayList<>();
			serverNames.add(this.getServer().getName());

			for (BukkitServer server : serverList.values())
				serverNames.add(server.getName());

			return Collections.unmodifiableList(serverNames);
		} else
			throw new UnsupportedOperationException(String.format("No %s listener available to query remote servers!", BUNGEE_CHANNEL));
	}

	public Set<BukkitServer> getServers() {
		return Collections.unmodifiableSet(new HashSet<>(serverList.values()));
	}

	public boolean isRegistered() {
		return Bukkit.getMessenger().isIncomingChannelRegistered(this.getPlugin(), BUNGEE_CHANNEL) && Bukkit.getMessenger().isOutgoingChannelRegistered(this.getPlugin(), BUNGEE_CHANNEL);
	}

	public boolean isOnline() {
		return bungeeOnline;
	}

	public void message(Player player, String targetPlayer, String message) {
		this.write(player, "Message", targetPlayer, message);
	}

	public void register() {
		if (this.isRegistered()) return;
		Bukkit.getMessenger().registerIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BUNGEE_CHANNEL, this);
	}

	public void uuid(Player player) {
		this.write(player, "UUID");
	}

	public void uuidOther(Player player, String targetPlayer) {
		this.write(player, "UUIDOther", targetPlayer);
	}

	private void write(Player player, String channel, Object... objs) {
		write(this.getPlugin(), player, channel, objs);
	}

	private static void write(JavaPlugin plugin, Player player, String channel, Object... objs) {
		if (!bungeeOnline()) return;
		if (channel.equals("Forward")) return;
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(channel);

		for (Object obj : objs) {
			if (obj instanceof String)
				out.writeUTF((String)obj);
			else if (obj instanceof Short)
				out.writeShort((short)obj);
			else if (obj instanceof Integer)
				out.writeInt((int)obj);
			else if (obj instanceof Boolean)
				out.writeBoolean((boolean)obj);
		}

		player.sendPluginMessage(plugin, BUNGEE_CHANNEL, out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(BUNGEE_CHANNEL)) return;

		try {
			ByteArrayDataInput input = ByteStreams.newDataInput(message);
			String subChannel = input.readUTF();
			if (subChannel.matches("^Player(?:Count|List)$")) return;

			if (subChannel.startsWith("NiftyBungee")) {
				short length = input.readShort();
				byte[] data = new byte[length];
				input.readFully(data);
				ByteArrayDataInput loader = ByteStreams.newDataInput(data);
				String serverName = loader.readUTF();
				BukkitServer server = serverList.get(serverName);

				if (subChannel.endsWith("ServerPull"))
					forward(this.getPlugin(), player, server.getName(), "NiftyBungeeServerPush", this.getServer().getName(), this.getServer().getAddress().getHostName(), this.getServer().getAddress().getPort());
				else if (subChannel.endsWith("ServerPush")) {
					if (server == null) {
						serverList.put(serverName, (server = new BukkitServer()));
						server.setName(serverName);
					}

					server.setAddress(loader.readUTF(), loader.readInt());
				}
			} else if (subChannel.equals("GetServer")) {
				final BukkitServer server = new BukkitServer();
				server.setName(input.readUTF());
				server.setAddress(Bukkit.getServer().getIp(), Bukkit.getServer().getPort());
				serverList.put(server.getName(), server);
				forward(this.getPlugin(), player, "ALL", "NiftyBungeeServerPush", this.getServer().getName(), this.getServer().getAddress().getHostName(), this.getServer().getAddress().getPort());
				write(this.getPlugin(), player, "GetServers");
			} else if (subChannel.equals("GetServers")) {
				final Set<String> serverNames = new HashSet<>(Arrays.asList(input.readUTF().split(", ")));

				for (String serverName : serverNames) {
					final BukkitServer server = (serverList.keySet().contains(serverName) ? this.getServer() : new BukkitServer());

					if (!serverList.keySet().contains(serverName)) {
						server.setName(serverName);
						serverList.put(serverName, server);
						forward(this.getPlugin(), player, server.getName(), "NiftyBungeeServerPull", this.getServer().getName());
					}

					LOADED_SERVERS++;
					Bukkit.getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Bukkit.getServer().getPluginManager().callEvent(new BungeeServerLoadedEvent(server));
							if (LOADED_SERVERS == serverNames.size()) Bukkit.getServer().getPluginManager().callEvent(new BungeeLoadedEvent(serverList.values()));
						}
					}, 5); // TODO: Find smallest wait time
				}
			} else if (subChannel.equals("ServerIP")) {
				
			} else {
				if (this.listener != null)
					this.listener.onMessageReceived(subChannel, player, message);
			}
		} catch (Exception ex) {
			this.getLog().console(ex);
		}

	}

	private static class BungeeChecker extends BukkitListener {

		public BungeeChecker(JavaPlugin plugin) {
			super(plugin);
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerChannelEvent(PlayerChannelEvent event) {
			if (event.getChannel().equalsIgnoreCase(BUNGEE_CHANNEL)) {
				event.getHandlers().unregister(this);
				BungeeHelper.bungeeOnline = true;
				Player player = event.getPlayer();
				write(this.getPlugin(), player, "GetServer");
			}
		}

	}

}
