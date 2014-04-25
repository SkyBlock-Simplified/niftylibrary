package net.netcoding.niftybukkit.minecraft;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.netcoding.niftybukkit.util.RegexUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Log {

	private Logger logger;

	public Log() {
		this(Bukkit.getLogger());
	}

	public Log(JavaPlugin plugin) {
		this(plugin.getLogger());
	}

	public Log(Logger logger) {
		this.logger = logger;
	}

	public String getPrefix(String text) {
		return StringUtil.format("{0}[{1}{2}{0}]{3}", ChatColor.DARK_GRAY, ChatColor.RED, text, ChatColor.GRAY);
	}

	public void console(Object... args) {
		console("", null, args);
	}

	public void console(Throwable exception, Object... args) {
		console("", exception, args);
	}

	public void console(String message, Object... args) {
		console(message, null, args);
	}

	public void console(String message, Throwable exception, Object... args) {
		message = StringUtil.isEmpty(message) ? "null" : message;
		message = RegexUtil.strip(StringUtil.format(RegexUtil.replace(message, RegexUtil.LOG_PATTERN), args), RegexUtil.VANILLA_PATTERN);

		if (exception != null)
			this.logger.log(Level.SEVERE, message, exception);
		else
			this.logger.log(Level.INFO, message);
	}

	public void error(CommandSender sender, Object... args) {
		error(sender, "", args);
	}

	public void error(CommandSender sender, String message, Object... args) {
		error(sender, message, null, args);
	}

	public void error(CommandSender sender, String message, Throwable exception, Object... args) {
		message(sender, StringUtil.format("{0} {1}", getPrefix("Error"), message), exception, args);
	}

	public void message(CommandSender sender, String message, Object... args) {
		message(sender, message, null, args);
	}

	public void message(CommandSender sender, String message, Throwable exception, Object... args) {
		boolean isConsole = BukkitHelper.isConsole(sender);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			console(message, exception, args);

		if (!isConsole)
			sender.sendMessage(parse(message, args));
	}

	public String parse(String message, Object... args) {
		return ChatColor.GRAY + StringUtil.format(message, args);
	}

}