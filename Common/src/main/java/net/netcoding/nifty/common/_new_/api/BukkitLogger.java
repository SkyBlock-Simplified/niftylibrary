package net.netcoding.nifty.common._new_.api;

import net.netcoding.nifty.common._new_.minecraft.entity.living.Player;
import net.netcoding.nifty.common._new_.minecraft.command.source.CommandSource;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.core.api.MinecraftLogger;
import net.netcoding.nifty.core.util.StringUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BukkitLogger extends MinecraftLogger {

	public static final BukkitLogger GLOBAL;

	static {
		GLOBAL = new BukkitLogger() {

			private final Logger logger = Logger.getGlobal();

			@Override
			protected void logInfo(String message) {
				this.logger.log(Level.INFO, message);
			}

			@Override
			protected void logSevere(String message, Throwable exception) {
				this.logger.log(Level.SEVERE, message, exception);
			}

			@Override
			protected void logWarn(String message) {
				this.logger.log(Level.WARNING, message);
			}

		};
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
		boolean isConsole = BukkitHelper.isConsole(sender);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			this.console(message, exception, args);

		if (!isConsole)
			sender.sendMessage(this.parse(message, args));
	}

}