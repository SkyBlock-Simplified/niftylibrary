package net.netcoding.nifty.common.api.plugin.messaging;

import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;

import java.util.Set;

public interface PluginMessageRecipient {

	Set<String> getListeningPluginChannels();

	void sendPluginMessage(MinecraftPlugin plugin, String channel, byte[] message);

}