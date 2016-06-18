package net.netcoding.nifty.craftbukkit.minecraft.source.command;

import net.netcoding.niftybukkit._new_.minecraft.BukkitServer;
import net.netcoding.niftybukkit._new_.minecraft.source.command.ConsoleCommandSource;
import net.netcoding.niftycore.util.json.JsonMessage;

public final class CraftConsoleCommandSource implements ConsoleCommandSource {

	private final BukkitServer server;

	public CraftConsoleCommandSource(BukkitServer server) {
		this.server = server;
	}

	@Override
	public BukkitServer getServer() {
		return this.server;
	}

	@Override
	public String getName() {
		return "CONSOLE";
	}

	@Override
	public boolean isPermissionSet(String permission) {
		return false; // TODO
	}

	@Override
	public boolean hasPermission(String permission) {
		return false; // TODO
	}

	@Override
	public boolean isOp() {
		return true;
	}

	@Override
	public void setOp(boolean value) {
		throw new UnsupportedOperationException("Cannot change operator status of server console!");
	}

	@Override
	public void sendMessage(JsonMessage message) throws Exception {
		this.sendRawMessage(message.toLegacyText());
	}

}