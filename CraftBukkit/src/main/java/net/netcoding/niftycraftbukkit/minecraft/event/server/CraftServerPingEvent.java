package net.netcoding.niftycraftbukkit.minecraft.event.server;

import net.netcoding.niftybukkit._new_.minecraft.event.server.BukkitServerPingEvent;
import net.netcoding.niftybukkit._new_.reflection.BukkitReflection;
import net.netcoding.niftybukkit._new_.reflection.MinecraftPackage;
import net.netcoding.niftybukkit._new_.reflection.MinecraftProtocol;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CraftServerPingEvent extends BukkitServerPingEvent {

	private final static transient Reflection SERVER_LIST_PING = new Reflection("PacketStatusListener$1ServerListPingEvent", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection PACKET_LISTENER = new Reflection("PacketStatusListener", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection MINECRAFT_SERVER = new Reflection("MinecraftServer", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection NETWORK_MANAGER = new Reflection("NetworkManager", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING = new Reflection("ServerPing", MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING_SAMPLE = BukkitReflection.getCompatibleReflection("ServerPing", "ServerPingPlayerSample");
	private final static transient Reflection SERVER_PING_DATA = new Reflection(StringUtil.format("ServerPing{0}ServerData", (MinecraftProtocol.isPre1_8_3() ? "" : "$")), MinecraftPackage.MINECRAFT_SERVER);
	private final static transient Reflection SERVER_PING_PACKET = new Reflection("PacketStatusOutServerInfo", MinecraftPackage.MINECRAFT_SERVER);

	private final Object minecraftServer;
	private final Object networkManager;

	public CraftServerPingEvent(ServerListPingEvent event) {
		super(event.getAddress(), event.getMotd(), event.getNumPlayers(), event.getMaxPlayers());
		Object packetListener = SERVER_LIST_PING.getValue(PACKET_LISTENER.getClazz(), event);
		this.minecraftServer = PACKET_LISTENER.getValue(MINECRAFT_SERVER.getClazz(), packetListener);
		this.networkManager = PACKET_LISTENER.getValue(NETWORK_MANAGER.getClazz(), packetListener);
	}

	@Override
	public final String getServerModName() {
		return (String)MINECRAFT_SERVER.invokeMethod("getServerModName", this.minecraftServer);
	}

	@Override
	public final <T extends SocketAddress> T getSocketAddress(Class<T> socket) {
		return socket.cast(NETWORK_MANAGER.invokeMethod("getSocketAddress", this.networkManager));
	}

	@Override
	public final String getVersion() {
		return (String)MINECRAFT_SERVER.invokeMethod("getVersion", this.minecraftServer);
	}

	@Override
	public final void sendSpoofedVersion(String name, int protocol, boolean merge) {
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