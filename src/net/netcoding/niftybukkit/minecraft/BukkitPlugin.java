package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

	private final transient Log log;

	public BukkitPlugin() {
		this.log = new Log(this);
	}

	public Log getLog() {
		return this.log;
	}

	public boolean hasPermissions(CommandSender sender, String... permissions) {
		if (isConsole(sender)) return true;
		return sender.hasPermission(String.format("%s.%s", this.getDescription().getName(), StringUtil.implode(".", permissions)));
	}

	public static boolean isConsole(CommandSender sender) {
		return !isPlayer(sender);
	}

	public static boolean isConsole(String senderName) {
		return !isPlayer(senderName);
	}

	public static boolean isPlayer(CommandSender sender) {
		return isPlayer(sender.getName());
	}

	public static boolean isPlayer(String senderName) {
		return !senderName.equalsIgnoreCase("console");
	}

}