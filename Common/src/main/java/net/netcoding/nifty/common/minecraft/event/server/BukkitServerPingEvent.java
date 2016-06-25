package net.netcoding.nifty.common.minecraft.event.server;

import net.netcoding.nifty.common.reflection.MinecraftProtocol;

import java.net.InetAddress;
import java.net.SocketAddress;

public abstract class BukkitServerPingEvent implements ServerEvent {

	private final static int PROTOCOL = MinecraftProtocol.getCurrentProtocol();
	private final InetAddress internalAddress;
	private String motd;
	private int numPlayers;
	private int maxPlayers;

	protected BukkitServerPingEvent(InetAddress internalAddress, String motd, int numPlayers, int maxPlayers) {
		this.internalAddress = internalAddress;
		this.motd = motd;
		this.numPlayers = numPlayers;
		this.maxPlayers = maxPlayers;
	}

	public final InetAddress getInetAddress() {
		return this.internalAddress;
	}

	public final int getMaxPlayers() {
		return this.maxPlayers;
	}

	public final int getNumPlayers() {
		return this.numPlayers;
	}

	public final String getMotd() {
		return this.motd;
	}

	public abstract String getServerModName();

	public final SocketAddress getSocketAddress() {
		return this.getSocketAddress(SocketAddress.class);
	}

	public abstract <T extends SocketAddress> T getSocketAddress(Class<T> socket);

	public abstract String getVersion();

	/**
	 * Will respond to the ping request with your own mod name and protocol.
	 * <p>
	 * Note that invoking this event will cancel the original response,
	 * and any modification after invocation future changes will be ignored.
	 *
	 * @param name Server mod name.
	 */
	public final void sendSpoofedVersion(String name) {
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
	public final void sendSpoofedVersion(String name, boolean merge) {
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
	public final void sendSpoofedVersion(String name, int protocol) {
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
	public abstract void sendSpoofedVersion(String name, int protocol, boolean merge);

	public final void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public final void setMotd(String motd) {
		this.motd = motd;
	}

}