package net.netcoding.niftybukkit.minecraft;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.netcoding.niftybukkit.utilities.RegexUtil;
import net.netcoding.niftybukkit.utilities.StringUtil;

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
		return String.format("%1$s[%2$s%3$s%1$s]%4$s", ChatColor.DARK_GRAY, ChatColor.RED, text, ChatColor.GRAY);
	}

	private String getPluginName() throws ClassNotFoundException {
		return Class.forName(this.logger.getName()).getSimpleName();
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
		if (exception != null)
			this.logger.log(Level.SEVERE, String.format(RegexUtil.replace(RegexUtil.strip(message, RegexUtil.VANILLA_PATTERN), RegexUtil.LOG_PATTERN), args), exception);
		else
			this.logger.log(Level.INFO, String.format(RegexUtil.replace(RegexUtil.strip(message, RegexUtil.VANILLA_PATTERN), RegexUtil.LOG_PATTERN), args));
	}

	public void error(CommandSender sender, Object... args) {
		error(sender, "", args);
	}

	public void error(CommandSender sender, String message, Object... args) {
		error(sender, message, null, args);
	}

	public void error(CommandSender sender, String message, Throwable exception, Object... args) {
		message(sender, String.format("%s %s", getPrefix("Error"), message), exception, args);
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

	public void noPerms(CommandSender sender, String... permissions) {
		String message = StringUtil.implode(".", permissions);

		try {
			message = String.format("%s.%s", this.getPluginName().toLowerCase(), message);
		} catch (ClassNotFoundException ex) { }

		message(sender, "You do not have the required permission {%1$s}", message);
	}

	public String parse(String message, Object... args) {
		return ChatColor.GRAY + String.format(RegexUtil.replace(message, RegexUtil.LOG_PATTERN, (ChatColor.RED + "$1" + ChatColor.GRAY)), args);
	}

}