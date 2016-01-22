package net.netcoding.niftybukkit.minecraft.messages;

public interface BungeeListener {

	void onMessageReceived(String channel, byte[] message) throws Exception;

}