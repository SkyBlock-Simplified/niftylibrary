package net.netcoding.nifty.common.minecraft.command;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.common.minecraft.permission.Permissible;
import net.netcoding.nifty.core.api.MessageRecipient;

public interface CommandSource extends Permissible, MessageRecipient {

	default Server getServer() {
		return Nifty.getServer();
	}

	String getName();

}