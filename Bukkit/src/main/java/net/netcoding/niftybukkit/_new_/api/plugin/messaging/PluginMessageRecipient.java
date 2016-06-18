package net.netcoding.niftybukkit._new_.api.plugin.messaging;

import net.netcoding.niftycore.api.plugin.Plugin;

import java.util.Set;

public interface PluginMessageRecipient {

	Set<String> getListeningPluginChannels();

	void sendPluginMessage(Plugin plugin, String channel, byte[] message);

}