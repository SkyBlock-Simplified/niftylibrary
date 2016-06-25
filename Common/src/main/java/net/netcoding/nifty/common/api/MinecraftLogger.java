package net.netcoding.nifty.common.api;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.IMinecraftPlugin;
import net.netcoding.nifty.common.minecraft.command.source.CommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.core.api.logger.BroadcasttLogger;
import net.netcoding.nifty.core.util.StringUtil;

public class MinecraftLogger extends BroadcasttLogger {

	public MinecraftLogger(IMinecraftPlugin plugin) {
		super(plugin);
	}

	@Override
	public final void broadcast(String message, Throwable exception, Object... args) {
		if (exception != null)
			this.console(exception);

		for (Player player : Nifty.getServer().getPlayerList())
			this.message(player, message, args);
	}

	public final void error(CommandSource sender, String message, Object... args) {
		this.error(sender, message, null, args);
	}

	public final void error(CommandSource sender, String message, Throwable exception, Object... args) {
		this.message(sender, StringUtil.format("{0}{1}", getPrefix("Error"), message), exception, args);
	}

	public final void message(CommandSource sender, String message, Object... args) {
		this.message(sender, message, null, args);
	}

	protected final void message(CommandSource sender, String message, Throwable exception, Object... args) {
		boolean isConsole = MinecraftHelper.isConsole(sender);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			this.console(message, exception, args);

		if (!isConsole)
			sender.sendMessage(this.parse(message, args));
	}

}