package net.netcoding.niftybukkit.minecraft;

import org.bukkit.entity.Player;

public interface BungeeListener {

	public void onMessageReceived(String channel, Player player, byte[] message) throws Exception;

}