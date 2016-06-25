package net.netcoding.nifty.craftbukkit.api.plugin.messaging;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common.api.plugin.messaging.ChannelWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

final class CraftNiftyChannelWrapper extends ChannelWrapper {

	private final JavaPlugin plugin;
	private final HiddenMessageListener hiddenListener;

	public CraftNiftyChannelWrapper() {
		super(Nifty.getPlugin(), BungeeHelper.NIFTY_CHANNEL, null);
		this.plugin = net.netcoding.nifty.common.Nifty.getServiceManager().getProvider(JavaPlugin.class);
		this.hiddenListener = new HiddenMessageListener();
	}

	public final JavaPlugin getJavaPlugin() {
		return this.plugin;
	}

	@Override
	public final void handle(String channel, byte[] message) {
		CraftBungeeHelper.getInstance().handleNiftyHook(channel, message);
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