package net.netcoding.nifty.common._new_.minecraft.source.command;

import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.permission.Permissible;
import net.netcoding.niftycore.api.MessageRecipient;

public interface CommandSource extends Permissible, MessageRecipient {

	BukkitServer getServer();

	String getName();

}