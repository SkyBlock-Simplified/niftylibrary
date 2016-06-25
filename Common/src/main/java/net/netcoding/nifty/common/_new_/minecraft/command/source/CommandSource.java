package net.netcoding.nifty.common._new_.minecraft.command.source;

import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.permission.Permissible;
import net.netcoding.nifty.core.api.MessageRecipient;

public interface CommandSource extends Permissible, MessageRecipient {

	BukkitServer getServer();

	String getName();

}