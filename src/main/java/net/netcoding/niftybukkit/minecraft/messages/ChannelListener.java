package net.netcoding.niftybukkit.minecraft.messages;

public interface ChannelListener {

	void onMessageReceived(String channel, byte[] message) throws Exception;

}