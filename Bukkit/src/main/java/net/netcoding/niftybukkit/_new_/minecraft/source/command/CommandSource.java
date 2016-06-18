package net.netcoding.niftybukkit._new_.minecraft.source.command;

import net.netcoding.niftybukkit._new_.minecraft.BukkitServer;
import net.netcoding.niftybukkit._new_.minecraft.permission.Permissible;
import net.netcoding.niftycore.api.MessageRecipient;

public interface CommandSource extends Permissible, MessageRecipient {

	BukkitServer getServer();

	String getName();

}