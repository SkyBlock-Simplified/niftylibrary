package net.netcoding.nifty.common.api.plugin.messaging;

public interface ChannelListener {

	void onMessageReceived(String channel, byte[] message) throws Exception;

}