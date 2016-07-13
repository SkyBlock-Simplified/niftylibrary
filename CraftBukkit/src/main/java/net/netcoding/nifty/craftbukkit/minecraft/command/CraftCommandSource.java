package net.netcoding.nifty.craftbukkit.minecraft.command;

import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.core.util.json.JsonMessage;

public abstract class CraftCommandSource implements CommandSource {

	private final org.bukkit.command.CommandSender sender;

	public CraftCommandSource(org.bukkit.command.CommandSender sender) {
		this.sender = sender;
	}

	public org.bukkit.command.CommandSender getHandle() {
		return this.sender;
	}

	@Override
	public final String getName() {
		return this.getHandle().getName();
	}

	@Override
	public final boolean isPermissionSet(String permission) {
		return this.getHandle().isPermissionSet(permission);
	}

	@Override
	public final boolean hasPermission(String permission) {
		return this.getHandle().hasPermission(permission);
	}

	@Override
	public final boolean isOp() {
		return this.getHandle().isOp();
	}

	@Override
	public final void setOp(boolean value) {
		this.getHandle().setOp(value);
	}

	@Override
	public void sendMessage(JsonMessage message) throws Exception {
		this.sendMessage(message.toLegacyText());
	}

	@Override
	public void sendMessage(String message) {
		this.getHandle().sendMessage(message);
	}

}