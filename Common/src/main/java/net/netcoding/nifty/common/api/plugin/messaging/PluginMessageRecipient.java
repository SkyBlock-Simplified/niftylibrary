package net.netcoding.nifty.common.api.plugin.messaging;

import net.netcoding.nifty.core.api.plugin.Plugin;

import java.util.Set;

public interface PluginMessageRecipient {

	Set<String> getListeningPluginChannels();

	void sendPluginMessage(Plugin plugin, String channel, byte[] message);

}