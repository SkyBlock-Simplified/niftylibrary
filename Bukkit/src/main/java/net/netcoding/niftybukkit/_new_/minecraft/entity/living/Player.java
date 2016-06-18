package net.netcoding.niftybukkit._new_.minecraft.entity.living;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.api.plugin.messaging.PluginMessageRecipient;
import net.netcoding.niftybukkit._new_.minecraft.BukkitServer;
import net.netcoding.niftybukkit._new_.minecraft.GameMode;
import net.netcoding.niftybukkit._new_.minecraft.OfflinePlayer;
import net.netcoding.niftybukkit._new_.minecraft.source.command.CommandSource;
import net.netcoding.niftycore.mojang.OnlineProfile;

import java.net.InetSocketAddress;

public interface Player extends CommandSource, HumanEntity, OfflinePlayer, OnlineProfile, PluginMessageRecipient {

	InetSocketAddress getAddress();

	GameMode getGameMode();

	default BukkitServer getServer() {
		return Nifty.getServer();
	}

	void setGameMode(GameMode gameMode);

	void updateInventory();

}