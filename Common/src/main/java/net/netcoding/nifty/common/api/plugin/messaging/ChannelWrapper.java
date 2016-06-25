package net.netcoding.nifty.common.api.plugin.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;

public class ChannelWrapper {

	private final MinecraftPlugin plugin;
	private final String channel;
	private final ChannelListener listener;

	public ChannelWrapper(MinecraftPlugin plugin, String channel, ChannelListener listener) {
		this.plugin = plugin;
		this.channel = channel;
		this.listener = listener;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChannelWrapper)) return false;
		if (obj == this) return true;
		ChannelWrapper wrapper = (ChannelWrapper)obj;
		return wrapper.getPlugin().equals(this.getPlugin()) && wrapper.getChannel().equalsIgnoreCase(this.getChannel());
	}

	public final MinecraftPlugin getPlugin() {
		return this.plugin;
	}

	public final String getChannel() {
		return this.channel;
	}

	public final ChannelListener getListener() {
		return this.listener;
	}

	protected void handle(String channel, byte[] message) {
		if (!this.isRegistered(channel)) return;
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();

		if (BungeeHelper.BUNGEE_CHANNEL.equals(channel) && subChannel.matches("^GetServers?|Player(?:Count|List|Update)|UUID(?:Other)?|(?:Server)?IP$"))
			return;

		try {
			this.listener.onMessageReceived(channel, message);
		} catch (Exception ex) {
			if (!(ex instanceof IllegalStateException))
				net.netcoding.nifty.common.Nifty.getPlugin().getLog().console(ex);
		}
	}

	@Override
	public int hashCode() {
		return 32 * this.getPlugin().hashCode() * this.getChannel().hashCode();
	}

	public final boolean isRegistered() {
		return this.isRegistered(this.getChannel());
	}

	protected boolean isRegistered(String channel) {
		return net.netcoding.nifty.common.Nifty.getMessenger().isIncomingChannelRegistered(this.getPlugin(), channel) && net.netcoding.nifty.common.Nifty.getMessenger().isOutgoingChannelRegistered(this.getPlugin(), channel);
	}

	public void register() {
		if (this.isRegistered()) return;
		net.netcoding.nifty.common.Nifty.getMessenger().registerIncomingPluginChannel(this.getPlugin(), this.getChannel(), this);
		net.netcoding.nifty.common.Nifty.getMessenger().registerOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (!this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				net.netcoding.nifty.common.Nifty.getMessenger().registerIncomingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL, this);
				net.netcoding.nifty.common.Nifty.getMessenger().registerOutgoingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	public void unregister() {
		if (!this.isRegistered()) return;
		net.netcoding.nifty.common.Nifty.getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), this.getChannel());
		net.netcoding.nifty.common.Nifty.getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				net.netcoding.nifty.common.Nifty.getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL);
				net.netcoding.nifty.common.Nifty.getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	public final void write(BukkitMojangProfile profile, String subChannel, Object... data) {
		net.netcoding.nifty.common.Nifty.getBungeeHelper().write(profile, this.getChannel(), subChannel, data);
	}

}