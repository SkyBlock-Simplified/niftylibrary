package net.netcoding.niftybukkit.minecraft.messages;

public interface BungeeListener {

	public void onMessageReceived(String channel, byte[] message) throws Exception;

}