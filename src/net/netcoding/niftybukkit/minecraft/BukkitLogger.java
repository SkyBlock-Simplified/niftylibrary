package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.RegexUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLogger {

	private final static transient ConcurrentHashMap<String, List<String>> PLUGINS = new ConcurrentHashMap<>();
	private final transient Logger logger;

	static {
		NiftyBukkit.getPlugin().getServer().getScheduler().runTaskLater(NiftyBukkit.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Plugin[] plugins = NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugins();

				for (Plugin plugin : plugins) {
					PLUGINS.put(plugin.getName(), new ArrayList<String>());
					plugin.getLogger().addHandler(new LogHandler(plugin));
				}
			}
		}, 1L);
	}

	public BukkitLogger(JavaPlugin plugin) {
		this.logger = plugin.getLogger();
	}

	public String getPrefix(String text) {
		return StringUtil.format("{0}[{1}{2}{0}]{3}", ChatColor.DARK_GRAY, ChatColor.RED, text, ChatColor.GRAY);
	}

	public final static Set<String> getPluginCache() {
		return PLUGINS.keySet();
	}

	public final static List<String> getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : Collections.<String>emptyList();
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

	private void message(CommandSender sender, String message, Throwable exception, Object... args) {
		boolean isConsole = BukkitHelper.isConsole(sender);

		if ((isConsole || exception != null) || (isConsole && exception == null))
			console(message, exception, args);

		if (!isConsole)
			sender.sendMessage(parse(message, args));
	}

	public String parse(String message, Object... args) {
		return ChatColor.GRAY + StringUtil.format(message, args);
	}

	private static class LogHandler extends Handler {

		private final transient Plugin plugin;

		public LogHandler(Plugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public void publish(LogRecord record) {
			if (record.getThrown() != null)
				PLUGINS.get(this.plugin.getName()).add(record.getThrown().getMessage());
		}

		@Override
		public void flush() { }

		@Override
		public void close() throws SecurityException { }
		
	}

}