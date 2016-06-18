package net.netcoding.nifty.common._new_.minecraft.entity.living;

import net.netcoding.nifty.common._new_.minecraft.OfflinePlayer;
import net.netcoding.nifty.common._new_.minecraft.source.command.CommandSource;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.plugin.messaging.PluginMessageRecipient;
import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.GameMode;
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