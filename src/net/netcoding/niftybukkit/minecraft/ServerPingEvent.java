package net.netcoding.niftybukkit.minecraft;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private final static int PROTOCOL = 5; // TODO: Protocol version

	private final Object networkManager;

	public ServerPingEvent(ServerListPingEvent event) throws Exception {
		super(event.getAddress(), event.getMotd(), event.getNumPlayers(), event.getMaxPlayers());
		Object packetListener = SERVER_LIST_PING.getValue(PACKET_LISTENER.getClazz(), event);
		this.networkManager = PACKET_LISTENER.getValue(NETWORK_MANAGER.getClazz(), packetListener);
	}

	public final String getServerModName() throws Exception {
		return (String)NETWORK_MANAGER.invokeMethod("getServerModName", this.networkManager);
	}

	public final SocketAddress getSocketAddress() throws Exception {
		return this.getSocketAddress(SocketAddress.class);
	}

	public final <T extends SocketAddress> T getSocketAddress(Class<T> socket) throws Exception {
		return socket.cast(NETWORK_MANAGER.invokeMethod("getSocketAddress", this.networkManager));
	}

	public final String getVersion() throws Exception {
		return (String)NETWORK_MANAGER.invokeMethod("getVersion", this.networkManager);
	}

	/**
	 * Will respond to the ping request with your own mod name and protocol.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 * 
	 * @param name Server mod name.
	 */
	public final void sendSpoofedVersion(String name) throws Exception {
		this.sendSpoofedVersion(name, PROTOCOL, false);
	}

	/**
	 * Will respond to the ping request with your own mod name and protocol,
	 * with the option to combine with the previous name.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 * 
	 * @param name  Server mod name.
	 * @param merge True to merge the previous name, otherwise false.
	 */
	public final void sendSpoofedVersion(String name, boolean merge) throws Exception {
		this.sendSpoofedVersion(name, PROTOCOL, merge);
	}

	/**
	 * Will respond to the ping request with your own mod name and protocol.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 * 
	 * @param name     Server mod name.
	 * @param protocol Server protocol version.
	 */
	public final void sendSpoofedVersion(String name, int protocol) throws Exception {
		this.sendSpoofedVersion(name, protocol, false);
	}

	/**
	 * Will respond to the ping request with your own mod name and protocol,
	 * with the option to combine with the previous name.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 * 
	 * @param name     Server mod name.
	 * @param protocol Server protocol version.
	 * @param merge    True to merge the previous name, otherwise false.
	 */
	public final void sendSpoofedVersion(String name, int protocol, boolean merge) throws Exception {
		Object pingObj = SERVER_PING.newInstance();
		SERVER_PING.invokeMethod("setPlayerSample", pingObj, SERVER_PING_SAMPLE.newInstance(Bukkit.getMaxPlayers(), 0));
		String mergedName = name;

		if (merge) {
			String[] previousArr = StringUtil.split(",", this.getServerModName());
			List<String> previousList = new ArrayList<>(Arrays.asList(previousArr));
			previousList.add(0, name);
			mergedName = StringUtil.format("{0} {1}", StringUtil.implode(",", previousList), this.getVersion());
		}

		SERVER_PING.invokeMethod("setServerInfo", pingObj, SERVER_PING_DATA.newInstance(mergedName, protocol));
		NETWORK_MANAGER.invokeMethod("handle", this.networkManager, SERVER_PING_PACKET.newInstance(pingObj));
	}

}