package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftycore.minecraft.MinecraftLogger;
import net.netcoding.niftycore.util.RegexUtil;
import net.netcoding.niftycore.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLogger extends MinecraftLogger {

	public BukkitLogger(JavaPlugin plugin) {
		super(plugin.getLogger());
	}

	@Override
	public void broadcast(String message, Throwable exception, Object... args) {
		message = StringUtil.isEmpty(message) ? "null" : message;
		message = StringUtil.format(RegexUtil.replace(message, RegexUtil.LOG_PATTERN), args);

		if (exception != null)
			this.console(exception);

		Bukkit.broadcastMessage(message);
	}

	public void error(CommandSender sender, Object... args) {
		this.error(sender, "", args);
	}

	public void error(CommandSender sender, String message, Object... args) {
		this.error(sender, message, null, args);
	}

	public void error(CommandSender sender, String message, Throwable exception, Object... args) {
		this.message(sender, StringUtil.format("{0}{1}", getPrefix("Error"), message), exception, args);
	}

	public void message(CommandSender sender, String message, Object... args) {
		this.message(sender, message, null, args);
	}

	private void message(CommandSender sender, String message, Throwable exception, Object... args) {
		boolean isConsole = BukkitHelper.isConsole(sender);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			this.console(message, exception, args);

		if (!isConsole)
			sender.sendMessage(this.parse(message, args));
	}

}