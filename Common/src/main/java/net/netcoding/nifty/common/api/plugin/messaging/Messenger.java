package net.netcoding.nifty.common.api.plugin.messaging;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;

import java.util.Map;

public final class Messenger {

	private static final transient Messenger INSTANCE = new Messenger();
	private final transient ConcurrentMap<MinecraftPlugin, ConcurrentMap<String, ChannelWrapper>> INCOMING_CHANNELS = Concurrent.newMap();
	private final transient ConcurrentMap<MinecraftPlugin, ConcurrentSet<String>> OUTGOING_CHANNELS = Concurrent.newMap();

	private Messenger() { }

	public static Messenger getInstance() {
		return INSTANCE;
	}

	public void dispatch(String channel, byte[] message) {
		for (Map.Entry<MinecraftPlugin, ConcurrentMap<String, ChannelWrapper>> entry : INCOMING_CHANNELS.entrySet())
			entry.getValue().get(channel).handle(channel, message);
	}

	private boolean isIncomingChannelRegistered(MinecraftPlugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return INCOMING_CHANNELS.containsKey(plugin);
	}

	public boolean isIncomingChannelRegistered(MinecraftPlugin plugin, String channel) {
		Preconditions.checkArgument(StringUtil.notEmpty(channel), "Channel cannot be NULL!");
		return isIncomingChannelRegistered(plugin) && INCOMING_CHANNELS.get(plugin).containsKey(channel);
	}

	private boolean isOutgoingChannelRegistered(MinecraftPlugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return OUTGOING_CHANNELS.containsKey(plugin);
	}

	public boolean isOutgoingChannelRegistered(MinecraftPlugin plugin, String channel) {
		Preconditions.checkArgument(StringUtil.notEmpty(channel), "Channel cannot be NULL!");
		return isOutgoingChannelRegistered(plugin) && OUTGOING_CHANNELS.get(plugin).contains(channel);
	}

	public void registerIncomingPluginChannel(MinecraftPlugin plugin, String channel, ChannelWrapper listener) {
		if (!this.isIncomingChannelRegistered(plugin, channel)) {
			if (!INCOMING_CHANNELS.containsKey(plugin))
				INCOMING_CHANNELS.put(plugin, Concurrent.newMap());

			INCOMING_CHANNELS.get(plugin).put(channel, listener);
		} else
			throw new IllegalArgumentException(StringUtil.format("Incoming channel ''{0}'' is already registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void registerOutgoingPluginChannel(MinecraftPlugin plugin, String channel) {
		if (!this.isOutgoingChannelRegistered(plugin, channel)) {
			if (!OUTGOING_CHANNELS.containsKey(plugin))
				OUTGOING_CHANNELS.put(plugin, Concurrent.newSet());

			OUTGOING_CHANNELS.get(plugin).add(channel);
		} else
			throw new IllegalArgumentException(StringUtil.format("Outgoing channel ''{0}'' is already registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void unregisterIncomingPluginChannel(MinecraftPlugin plugin) {
		if (this.isIncomingChannelRegistered(plugin))
			INCOMING_CHANNELS.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{1}'' has no registered incoming channels!", plugin.getName()));
	}

	public void unregisterIncomingPluginChannel(MinecraftPlugin plugin, String channel) {
		if (this.isIncomingChannelRegistered(plugin, channel))
			INCOMING_CHANNELS.get(plugin).remove(channel);
		else
			throw new IllegalArgumentException(StringUtil.format("Incoming channel ''{0}'' is not registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void unregisterOutgoingPluginChannel(MinecraftPlugin plugin) {
		if (this.isOutgoingChannelRegistered(plugin))
			OUTGOING_CHANNELS.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{1}'' has no registered outgoing channels!", plugin.getName()));
	}

	public void unregisterOutgoingPluginChannel(MinecraftPlugin plugin, String channel) {
		if (this.isOutgoingChannelRegistered(plugin, channel))
			OUTGOING_CHANNELS.get(plugin).remove(channel);
		else
			throw new IllegalArgumentException(StringUtil.format("Outgoing channel ''{0}'' is not registered for plugin ''{1}''!", channel, plugin.getName()));
	}

}