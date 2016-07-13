package net.netcoding.nifty.craftbukkit.minecraft.command;

import net.netcoding.nifty.common.minecraft.command.ConsoleCommandSource;
import net.netcoding.nifty.core.util.json.JsonMessage;

public final class CraftConsoleCommandSource extends CraftCommandSource implements ConsoleCommandSource {

	public CraftConsoleCommandSource(org.bukkit.command.ConsoleCommandSender sender) {
		super(sender);
	}

	@Override
	public org.bukkit.command.ConsoleCommandSender getHandle() {
		return (org.bukkit.command.ConsoleCommandSender)super.getHandle();
	}

	@Override
	public void sendMessage(JsonMessage message) throws Exception {
		ConsoleCommandSource.super.sendMessage(message);
	}

}