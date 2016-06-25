package net.netcoding.nifty.common.minecraft.command.source;

import net.netcoding.nifty.common.minecraft.BukkitServer;
import net.netcoding.nifty.common.minecraft.permission.Permissible;
import net.netcoding.nifty.core.api.MessageRecipient;

public interface CommandSource extends Permissible, MessageRecipient {

	BukkitServer getServer();

	String getName();

}