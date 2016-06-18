package net.netcoding.niftybukkit._new_.api.plugin.messaging;

public interface ChannelListener {

	void onMessageReceived(String channel, byte[] message) throws Exception;

}