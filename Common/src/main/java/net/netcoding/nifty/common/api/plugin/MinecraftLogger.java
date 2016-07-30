package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.core.api.logger.BroadcastLogger;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.core.util.StringUtil;

public class MinecraftLogger extends BroadcastLogger {

	public MinecraftLogger(PluginDescription desc) {
		super(desc);
	}

	@Override
	public final void broadcast(String message, Throwable exception, Object... args) {
		if (exception != null)
			this.console(exception);

		for (Player player : Nifty.getServer().getPlayerList())
			this.message(player, message, args);
	}

	public final void error(CommandSource source, String message, Object... args) {
		this.error(source, message, null, args);
	}

	public final void error(CommandSource source, String message, Throwable exception, Object... args) {
		this.message(source, StringUtil.format("{0}{1}", getPrefix("Error"), message), exception, args);
	}

	public final void message(CommandSource source, String message, Object... args) {
		this.message(source, message, null, args);
	}

	protected final void message(CommandSource source, String message, Throwable exception, Object... args) {
		boolean isConsole = MinecraftHelper.isConsole(source);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			this.console(message, exception, args);

		if (!isConsole)
			source.sendMessage(this.parse(message, args));
	}

}