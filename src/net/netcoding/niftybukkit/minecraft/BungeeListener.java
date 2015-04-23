package net.netcoding.niftybukkit.minecraft;


public interface BungeeListener {

	public void onMessageReceived(String channel, byte[] message) throws Exception;

}