package net.netcoding.niftybukkit.minecraft.messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ChannelWrapper {

	private final JavaPlugin plugin;
	private final String channel;
	private final ChannelListener listener;
	private final HiddenMessageListener hiddenListener;

	ChannelWrapper(JavaPlugin plugin, String channel, ChannelListener listener) {
		this.plugin = plugin;
		this.channel = channel;
		this.listener = listener;
		this.hiddenListener = new HiddenMessageListener();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChannelWrapper)) return false;
		if (obj == this) return true;
		ChannelWrapper wrapper = (ChannelWrapper)obj;
		return wrapper.getPlugin().equals(this.getPlugin()) && wrapper.getChannel().equalsIgnoreCase(this.getChannel());
	}

	public final JavaPlugin getPlugin() {
		return this.plugin;
	}

	public final String getChannel() {
		return this.channel;
	}

	public final ChannelListener getListener() {
		return this.listener;
	}

	protected void handle(String channel, byte[] message) {
		if (!this.isRegistered()) return;
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String subChannel = input.readUTF();

		if (BungeeHelper.BUNGEE_CHANNEL.equals(channel) && subChannel.matches("^GetServers?|Player(?:Count|List|Update)|UUID(?:Other)?|(?:Server)?IP$"))
			return;

		try {
			this.listener.onMessageReceived(channel, message);
		} catch (Exception ex) {
			if (!(ex instanceof IllegalStateException))
				NiftyBukkit.getPlugin().getLog().console(ex);
		}
	}

	@Override
	public int hashCode() {
		return 32 * this.getPlugin().hashCode() * this.getChannel().hashCode();
	}

	public final boolean isRegistered() {
		return this.isRegistered(this.getChannel());
	}

	private boolean isRegistered(String channel) {
		return this.getPlugin().getServer().getMessenger().isIncomingChannelRegistered(this.getPlugin(), channel) && this.getPlugin().getServer().getMessenger().isOutgoingChannelRegistered(this.getPlugin(), channel);
	}

	public final void register() {
		if (this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), this.getChannel(), this.hiddenListener);
		this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (!this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				this.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL, this.hiddenListener);
				this.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	public final void unregister() {
		if (!this.isRegistered()) return;
		this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), this.getChannel(), this.hiddenListener);
		this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				this.getPlugin().getServer().getMessenger().unregisterIncomingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL, this.hiddenListener);
				this.getPlugin().getServer().getMessenger().unregisterOutgoingPluginChannel(this.getPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	public final void write(BukkitMojangProfile profile, String subChannel, Object... data) {
		NiftyBukkit.getBungeeHelper().write(profile, this.getChannel(), subChannel, data);
	}

	private class HiddenMessageListener implements PluginMessageListener {

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {
			ChannelWrapper.this.handle(channel, message);
		}

	}

}