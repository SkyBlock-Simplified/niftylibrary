package net.netcoding.niftycraftbukkit.api.plugin.messaging;

import net.netcoding.niftybukkit._new_.api.plugin.messaging.ChannelWrapper;
import net.netcoding.niftybukkit._new_.api.plugin.messaging.BungeeHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

final class CraftNiftyChannelWrapper extends ChannelWrapper.Nifty {

	private final JavaPlugin plugin;
	private final HiddenMessageListener hiddenListener;

	public CraftNiftyChannelWrapper() {
		this.plugin = net.netcoding.niftybukkit.Nifty.getServiceManager().getProvider(JavaPlugin.class);
		this.hiddenListener = new HiddenMessageListener();
	}

	public final JavaPlugin getJavaPlugin() {
		return this.plugin;
	}

	@Override
	protected final boolean isRegistered(String channel) {
		return Bukkit.getMessenger().isIncomingChannelRegistered(this.getJavaPlugin(), channel) && Bukkit.getMessenger().isOutgoingChannelRegistered(this.getJavaPlugin(), channel);
	}

	@Override
	public final void register() {
		if (this.isRegistered()) return;
		Bukkit.getMessenger().registerIncomingPluginChannel(this.getJavaPlugin(), this.getChannel(), this.hiddenListener);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this.getJavaPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (!this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				Bukkit.getMessenger().registerIncomingPluginChannel(this.getJavaPlugin(), BungeeHelper.BUNGEE_CHANNEL, this.hiddenListener);
				Bukkit.getMessenger().registerOutgoingPluginChannel(this.getJavaPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	@Override
	public final void unregister() {
		if (!this.isRegistered()) return;
		Bukkit.getMessenger().unregisterIncomingPluginChannel(this.getJavaPlugin(), this.getChannel(), this.hiddenListener);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.getJavaPlugin(), this.getChannel());

		if (!BungeeHelper.BUNGEE_CHANNEL.equals(this.getChannel())) {
			if (this.isRegistered(BungeeHelper.BUNGEE_CHANNEL)) {
				Bukkit.getMessenger().unregisterIncomingPluginChannel(this.getJavaPlugin(), BungeeHelper.BUNGEE_CHANNEL, this.hiddenListener);
				Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.getJavaPlugin(), BungeeHelper.BUNGEE_CHANNEL);
			}
		}
	}

	private class HiddenMessageListener implements PluginMessageListener {

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {
			CraftNiftyChannelWrapper.this.handle(channel, message);
		}

	}

}