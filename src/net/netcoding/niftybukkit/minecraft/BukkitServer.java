package net.netcoding.niftybukkit.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.DataUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

import com.google.common.io.ByteArrayDataOutput;
import com.google.gson.Gson;

@SuppressWarnings("unused")
public class BukkitServer extends MinecraftServer {

	private static final transient Gson GSON = new Gson();
	private transient int socketTimeout = 2000;
	private final transient ServerPingListener listener;

	public BukkitServer(String ip, ServerPingListener listener) {
		this(ip, 25565, listener);
	}

	public BukkitServer(String ip, int port, ServerPingListener listener) {
		this.setAddress(ip, port);
		this.listener = listener;
	}

	public int getSocketTimeout() {
		return this.socketTimeout;
	}

	public void ping() {
		if (this.getAddress() == null) return;
		final BukkitServer that = this;

		NiftyBukkit.getPlugin().getServer().getScheduler().runTaskAsynchronously(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				try (Socket socket = new Socket()) {
					socket.setSoTimeout(getSocketTimeout());
					socket.connect(getAddress(), getSocketTimeout());

					try (OutputStream outputStream = socket.getOutputStream()) {
						try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
							try (InputStream inputStream = socket.getInputStream()) {
								try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
									DataUtil.writeByteArray(dataOutputStream, prepareHandshake());
									DataUtil.writeByteArray(dataOutputStream, preparePing());

									try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
										StatusResponse response = processResponse(dataInputStream);
			
										setMotd(response.getMotd());
										setGameVersion(response.getVersion().getName());
										setProtocolVersion(response.getVersion().getProtocol());
										setMaxPlayers(response.getPlayers().getMax());
										setOnline(true);
										StatusResponse.Players players = response.getPlayers();

										if (players != null) {
											if (players.getSample() != null) {
												List<String> current = new ArrayList<>();

												for (StatusResponse.Players.Player player : players.getSample())
													current.add(player.getName());

												ConcurrentSet<MojangProfile> profiles = new ConcurrentSet<>(Arrays.asList(NiftyBukkit.getMojangRepository().searchByUsername(current)));

												for (MojangProfile profile : playerList) {
													if (profiles.contains(profile))
														profiles.remove(profile);
													else
														playerList.remove(profile);
												}

												playerList.addAll(profiles);
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception ex) {
					setOnline(false);
					reset();
				} finally {
					if (listener != null)
						listener.onServerPing(that);
				}
			}
		});
	}

	public void setAddress(String ip, int port) {
		this.setAddress(InetSocketAddress.createUnresolved(ip, port));
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void setName(String serverName) {
		this.serverName = serverName;
	}

	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}

    private byte[] preparePing() throws IOException {
        return new byte[] { 0x00 };
    }

    private byte[] prepareHandshake() throws IOException {
    	ByteArrayDataOutput handshake = DataUtil.newDataOutput();
		handshake.writeByte(0x00);
		DataUtil.writeVarInt(handshake, 4);
		DataUtil.writeString(handshake, getAddress().getHostString());
		handshake.writeShort(getAddress().getPort());
		DataUtil.writeVarInt(handshake, 1);
		return handshake.toByteArray();
    }

    private StatusResponse processResponse(DataInputStream input) throws IOException {
		int size = DataUtil.readVarInt(input);

		int id = DataUtil.readVarInt(input);
		if (id != 0) throw new IOException("Invalid packetID.");

		int length = DataUtil.readVarInt(input);
		if (length < 1) throw new IOException("Invalid string length.");

		byte[] data = new byte[length];
		input.readFully(data);
		return GSON.fromJson(new String(data, Charset.forName("UTF-8")), StatusResponse.class);
    }

	private class StatusResponse {

		private String description;
		private Players players;
		private Version version;
		private String favicon;

		public String getMotd() {
			return this.description;
		}

		public Players getPlayers() {
			return this.players;
		}

		public Version getVersion() {
			return this.version;
		}

		public String getFavicon() {
			return this.favicon;
		}

		public class Players {

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

			public class Player {

				private String name;
				private String id;

				public String getName() {
					return name;
				}

				public String getId() {
					return id;
				}

			}

		}

		public class Version {

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