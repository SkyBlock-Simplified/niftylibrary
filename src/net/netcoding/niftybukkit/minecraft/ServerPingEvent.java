package net.netcoding.niftybukkit.minecraft;

import java.net.SocketAddress;

import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.Reflection;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingEvent extends ServerListPingEvent {

	private final static transient Reflection SERVER_LIST_PING = new Reflection("PacketStatusListener$1ServerListPingEvent", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection PACKET_LISTENER = new Reflection("PacketStatusListener", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection NETWORK_MANAGER = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING = new Reflection("ServerPing", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING_SAMPLE = new Reflection("ServerPingPlayerSample", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING_DATA = new Reflection(StringUtil.format("ServerPing{0}ServerData", (MinecraftPackage.IS_PRE_1_8_3 ? "" : ".")), MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING_PACKET = new Reflection("PacketStatusOutServerInfo", MinecraftPackage.MINECRAFT_SERVER);

	private final Object networkManager;

	public ServerPingEvent(ServerListPingEvent event) throws Exception {
		super(event.getAddress(), event.getMotd(), event.getNumPlayers(), event.getMaxPlayers());
		Object packetListener = SERVER_LIST_PING.getValue(PACKET_LISTENER.getClazz(), event);
		this.networkManager = PACKET_LISTENER.getValue(NETWORK_MANAGER.getClazz(), packetListener);
	}

	public SocketAddress getSocketAddress() throws Exception {
		return this.getSocketAddress(SocketAddress.class);
	}

	public <T extends SocketAddress> T getSocketAddress(Class<T> socket) throws Exception {
		return socket.cast(NETWORK_MANAGER.invokeMethod("getSocketAddress", this.networkManager));
	}

	/**
	 * Will respond to the ping request with your own mod name and version.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 * 
	 * @param name Server mod name.
	 * @param protocol Server protocol version.
	 */
	public void sendSpoofedVersion(String name, int protocol) throws Exception {
		Object pingObj = SERVER_PING.newInstance();
		SERVER_PING.invokeMethod("setPlayerSample", pingObj, SERVER_PING_SAMPLE.newInstance(Bukkit.getMaxPlayers(), 0));
		SERVER_PING.invokeMethod("setServerInfo", pingObj, SERVER_PING_DATA.newInstance(name, protocol));
		NETWORK_MANAGER.invokeMethod("handle", this.networkManager, SERVER_PING_PACKET.newInstance(pingObj));
	}

}