package net.netcoding.nifty.common._new_.api.plugin.messaging;

public interface ChannelListener {

	void onMessageReceived(String channel, byte[] message) throws Exception;

}