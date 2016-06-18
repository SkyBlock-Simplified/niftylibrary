package net.netcoding.nifty.common._new_.api.plugin.messaging;

import com.google.common.base.Preconditions;
import net.netcoding.niftycore.api.plugin.Plugin;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

import java.util.Map;

public final class Messenger {

	private final transient ConcurrentMap<Plugin, ConcurrentMap<String, ChannelWrapper>> INCOMING_CHANNELS = new ConcurrentMap<>();
	private final transient ConcurrentMap<Plugin, ConcurrentSet<String>> OUTGOING_CHANNELS = new ConcurrentMap<>();
	private static transient Messenger INSTANCE;

	protected Messenger() { }

	public static Messenger getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Messenger();

		return INSTANCE;
	}

	public void dispatch(String channel, byte[] message) {
		for (Map.Entry<Plugin, ConcurrentMap<String, ChannelWrapper>> entry : INCOMING_CHANNELS.entrySet())
			entry.getValue().get(channel).handle(channel, message);
	}

	private boolean isIncomingChannelRegistered(Plugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return INCOMING_CHANNELS.containsKey(plugin);
	}

	public boolean isIncomingChannelRegistered(Plugin plugin, String channel) {
		Preconditions.checkArgument(StringUtil.notEmpty(channel), "Channel cannot be NULL!");
		return isIncomingChannelRegistered(plugin) && INCOMING_CHANNELS.get(plugin).containsKey(channel);
	}

	private boolean isOutgoingChannelRegistered(Plugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return OUTGOING_CHANNELS.containsKey(plugin);
	}

	public boolean isOutgoingChannelRegistered(Plugin plugin, String channel) {
		Preconditions.checkArgument(StringUtil.notEmpty(channel), "Channel cannot be NULL!");
		return isOutgoingChannelRegistered(plugin) && OUTGOING_CHANNELS.get(plugin).contains(channel);
	}

	public void registerIncomingPluginChannel(Plugin plugin, String channel, ChannelWrapper listener) {
		if (!this.isIncomingChannelRegistered(plugin, channel)) {
			if (!INCOMING_CHANNELS.containsKey(plugin))
				INCOMING_CHANNELS.put(plugin, new ConcurrentMap<>());

			INCOMING_CHANNELS.get(plugin).put(channel, listener);
		} else
			throw new IllegalArgumentException(StringUtil.format("Incoming channel ''{0}'' is already registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void registerOutgoingPluginChannel(Plugin plugin, String channel) {
		if (!this.isOutgoingChannelRegistered(plugin, channel)) {
			if (!OUTGOING_CHANNELS.containsKey(plugin))
				OUTGOING_CHANNELS.put(plugin, new ConcurrentSet<>());

			OUTGOING_CHANNELS.get(plugin).add(channel);
		} else
			throw new IllegalArgumentException(StringUtil.format("Outgoing channel ''{0}'' is already registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void unregisterIncomingPluginChannel(Plugin plugin) {
		if (this.isIncomingChannelRegistered(plugin))
			INCOMING_CHANNELS.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{1}'' has no registered incoming channels!", plugin.getName()));
	}

	public void unregisterIncomingPluginChannel(Plugin plugin, String channel) {
		if (this.isIncomingChannelRegistered(plugin, channel))
			INCOMING_CHANNELS.get(plugin).remove(channel);
		else
			throw new IllegalArgumentException(StringUtil.format("Incoming channel ''{0}'' is not registered for plugin ''{1}''!", channel, plugin.getName()));
	}

	public void unregisterOutgoingPluginChannel(Plugin plugin) {
		if (this.isOutgoingChannelRegistered(plugin))
			OUTGOING_CHANNELS.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{1}'' has no registered outgoing channels!", plugin.getName()));
	}

	public void unregisterOutgoingPluginChannel(Plugin plugin, String channel) {
		if (this.isOutgoingChannelRegistered(plugin, channel))
			OUTGOING_CHANNELS.get(plugin).remove(channel);
		else
			throw new IllegalArgumentException(StringUtil.format("Outgoing channel ''{0}'' is not registered for plugin ''{1}''!", channel, plugin.getName()));
	}

}