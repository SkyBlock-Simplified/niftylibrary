package net.netcoding.niftybukkit.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.util.com.google.gson.Gson;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentIHashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("unused")
public class BukkitServer {

	private final transient UUID uuid = UUID.randomUUID();
	private static final transient ConcurrentIHashMap<BukkitServer> serverList = new ConcurrentIHashMap<>();
	private static final transient int PING_INTERVAL = 5;
	private static final transient Gson gson = new Gson();
	private InetSocketAddress address;
	private transient int socketTimeout = 7000;
	private String serverName = "";
	private int pingVersion = -1;
	private int protocolVersion = -1;
	private String gameVersion = "";
	private String motd = "";
	private boolean online = false;
	private int playerCount = -1;
	private int maxPlayers = -1;
	protected Set<String> playerList = new HashSet<>();
	private transient BukkitTask task;

	static {
		Bukkit.getScheduler().runTaskTimerAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (BukkitServer server : serverList.values())
					server.fetchData();
			}
		}, 0, PING_INTERVAL);
	}

	BukkitServer() {
		serverList.put(uuid.toString(), this);
	}

	public BukkitServer(String ip, int port) {
		this.setAddress(ip, port);
		serverList.put(uuid.toString(), this);
	}

	private static int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}
		return i;
	}

	private static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

	private static void closeStreams(DataOutputStream dataOutputStream, OutputStream outputStream, InputStreamReader inputStreamReader, InputStream inputStream, Socket socket) {
		try { dataOutputStream.close(); } catch (IOException ex) { }
		try { outputStream.close(); } catch (IOException ex) { }
		try { inputStreamReader.close(); } catch (IOException ex) { }
		try { inputStream.close(); } catch (IOException ex) { }
		try { socket.close(); } catch (IOException ex) { }
	}

	private void fetchData() {
		if (this.getAddress() == null) return;

		Socket socket = null;
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		DataInputStream dataInputStream = null;

		try {
			socket = new Socket();
			socket.setSoTimeout(this.getSocketTimeout());
			socket.connect(this.getAddress(), this.getSocketTimeout());

			outputStream = socket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);

			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream handshake = new DataOutputStream(b);
			handshake.writeByte(0);
			writeVarInt(handshake, 4);
			writeVarInt(handshake, this.getAddress().getHostString().length());
			handshake.writeBytes(this.getAddress().getHostString());
			handshake.writeShort(this.getAddress().getPort());
			writeVarInt(handshake, 1);

			writeVarInt(dataOutputStream, b.size());
			dataOutputStream.write(b.toByteArray());

			dataOutputStream.writeByte(1);
			dataOutputStream.writeByte(0);
			dataInputStream = new DataInputStream(inputStream);
			int size = readVarInt(dataInputStream);

			int id = readVarInt(dataInputStream);
			if (id == -1) throw new IOException("Premature end of stream.");
			if (id != 0) throw new IOException("Invalid packetID");

			int length = readVarInt(dataInputStream);
			if (length == -1) throw new IOException("Premature end of stream.");
			if (length == 0) throw new IOException("Invalid string length.");

			byte[] in = new byte[length];
			dataInputStream.readFully(in);
			String json = new String(in);
			StatusResponse response = (StatusResponse)gson.fromJson(json, StatusResponse.class);

			try {
				long now = System.currentTimeMillis();
				dataOutputStream.writeByte(9);
				dataOutputStream.writeByte(1);
				dataOutputStream.writeLong(now);

				readVarInt(dataInputStream);
				id = readVarInt(dataInputStream);

				if (id == -1) {
					closeStreams(dataOutputStream, dataOutputStream, inputStreamReader, dataInputStream, socket);
					throw new IOException("Premature end of stream.");
				}

				if (id != 1) {
					closeStreams(dataOutputStream, dataOutputStream, inputStreamReader, dataInputStream, socket);
					throw new IOException("Invalid packetID");
				}

				long pingtime = dataInputStream.readLong();
				response.setTime((int)(now - pingtime));
			} catch (IOException exception) { }

			this.setMotd(response.getMotd());
			this.setGameVersion(response.getVersion().getName());
			this.setProtocolVersion(response.getVersion().getProtocol());
			this.setMaxPlayers(response.getPlayers().getMax());
			this.setPlayerCount(response.getPlayers().getOnline());
			this.online = true;
			this.playerList.clear();
			StatusResponse.Players players = response.getPlayers();

			if (players.getSample() != null) {
				for (StatusResponse.Player player : players.getSample())
					this.playerList.add(player.getName());
			}
		} catch (IOException ex) {
			this.reset();
		} catch (Exception ex) {
			this.reset();
			serverList.remove(uuid.toString());
			throw ex;
		} finally {
			closeStreams(dataOutputStream, dataOutputStream, inputStreamReader, dataInputStream, socket);
		}
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}

	public String getGameVersion() {
		return this.gameVersion;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public String getMotd() {
		return this.motd;
	}

	public String getName() {
		return this.serverName;
	}

	public int getPingVersion() {
		return this.pingVersion;
	}

	public int getPlayerCount() {
		return this.playerCount;
	}

	public List<String> getPlayerList() {
		return Collections.unmodifiableList(Arrays.asList(this.playerList.toArray(new String[this.playerList.size()])));
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public int getSocketTimeout() {
		return this.socketTimeout;
	}

	public boolean isCurrentServer() {
		return this.getAddress() != null && Bukkit.getServer().getIp().equals(this.getAddress().getHostName()) && this.getAddress().getPort() == Bukkit.getServer().getPort();
	}

	public boolean isOnline() {
		return this.online;
	}

	private void reset() {
		this.pingVersion = -1;
		this.protocolVersion = -1;
		this.gameVersion = "";
		this.motd = "";
		this.playerCount = -1;
		this.maxPlayers = -1;
		this.playerList.clear();
	}

	protected void setAddress(String ip, int port) {
		this.address = new InetSocketAddress(ip, port);
	}

	private void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}

	private void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	private void setMotd(String motd) {
		this.motd = motd;
	}

	public void setName(String serverName) {
		this.serverName = serverName;
	}

	private void setPingVersion(int pingVersion) {
		this.pingVersion = pingVersion;
	}

	private void setPlayerCount(int playersOnline) {
		this.playerCount = playersOnline;
	}

	private void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}

	private static class StatusResponse {

		private String description;
		private Players players;
		private Version version;
		private String favicon;
		private int time;

		public String getMotd() {
			return description;
		}

		public Players getPlayers() {
			return players;
		}

		public Version getVersion() {
			return version;
		}

		public String getFavicon() {
			return favicon;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}

		protected static class Players {

			private int max;
			private int online;
			private List<Player> sample;

			public int getMax() {
				return max;
			}

			public int getOnline() {
				return online;
			}

			public List<Player> getSample() {
				return sample;
			}

		}

		protected static class Player {

			private String name;
			private String id;

			public String getName() {
				return name;
			}

			public String getId() {
				return id;
			}

		}

		protected static class Version {

			private String name;
			private int protocol;

			public String getName() {
				return name;
			}

			public int getProtocol() {
				return protocol;
			}

		}

	}

}