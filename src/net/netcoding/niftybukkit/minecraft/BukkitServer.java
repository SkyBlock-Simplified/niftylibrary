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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.com.google.gson.Gson;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.NumberUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentSet;

@SuppressWarnings("unused")
public class BukkitServer extends MinecraftServer {

	private static final transient int PING_INTERVAL = 20;
	private static final transient Gson gson = new Gson();
	private transient int socketTimeout = 5000;
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
									try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
										try (DataOutputStream handshake = new DataOutputStream(b)) {
											handshake.writeByte(0);
											NumberUtil.writeVarInt(handshake, 4);
											NumberUtil.writeVarInt(handshake, getAddress().getHostString().length());
											handshake.writeBytes(getAddress().getHostString());
											handshake.writeShort(getAddress().getPort());
											NumberUtil.writeVarInt(handshake, 1);
										}

										NumberUtil.writeVarInt(dataOutputStream, b.size());
										dataOutputStream.write(b.toByteArray());
									}

									dataOutputStream.writeByte(1);
									dataOutputStream.writeByte(0);

									try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
										int size = NumberUtil.readVarInt(dataInputStream);
			
										int id = NumberUtil.readVarInt(dataInputStream);
										if (id == -1) throw new IOException("Premature end of stream.");
										if (id != 0) throw new IOException("Invalid packetID.");
			
										int length = NumberUtil.readVarInt(dataInputStream);
										if (length == -1) throw new IOException("Premature end of stream.");
										if (length == 0) throw new IOException("Invalid string length.");
			
										byte[] in = new byte[length];
										dataInputStream.readFully(in);
										String json = new String(in);
										StatusResponse response = gson.fromJson(json, StatusResponse.class);
			
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

										if (listener != null)
											listener.onServerPing(that);
									}
								}
							}
						}
					}
				} catch (Exception ex) {
					reset();
				}
			}
		});
	}

	public void setAddress(String ip, int port) {
		this.setAddress(new InetSocketAddress(ip, port));
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