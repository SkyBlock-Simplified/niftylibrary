package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitHelper {

	private final transient JavaPlugin javaPlugin;
	private final transient Log logger;

	public BukkitHelper(JavaPlugin plugin) {
		this.javaPlugin = plugin;
		this.logger = new Log(plugin.getLogger());
	}

	public Log getLog() {
		return this.logger;
	}

	public JavaPlugin getPlugin() {
		return this.javaPlugin;
	}

	public <T extends JavaPlugin> T getPlugin(Class<T> plugin) {
		return plugin.cast(this.getPlugin());
	}

	public PluginDescriptionFile getPluginDescription() {
		return this.getPlugin().getDescription();
	}

	public boolean hasPermissions(MojangProfile profile, String... permissions) {
		return this.hasPermissions(profile, false, permissions);
	}

	public boolean hasPermissions(MojangProfile profile, boolean defaultError, String... permissions) {
		return profile.getOfflinePlayer().isOnline() ? this.hasPermissions(profile.getOfflinePlayer().getPlayer(), defaultError, permissions) : false;
	}

	public boolean hasPermissions(CommandSender sender, String... permissions) {
		return this.hasPermissions(sender, false, permissions);
	}

	public boolean hasPermissions(CommandSender sender, boolean defaultError, String... permissions) {
		if (isConsole(sender)) return true;
		String permission = StringUtil.format("{0}.{1}", this.getPlugin().getDescription().getName().toLowerCase(), StringUtil.implode(".", permissions));
		boolean hasPerms = sender.hasPermission(permission);
		if (!hasPerms && defaultError) this.noPerms(sender, permission);
		return hasPerms;
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

	@Deprecated
	public static Player findPlayer(String playerName) {
		playerName = playerName.toLowerCase();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(playerName))
				return player;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().toLowerCase().startsWith(playerName))
				return player;
		}

		return null;
	}

	void noPerms(CommandSender sender, String permission) {
		this.getLog().error(sender, "You do not have the permission {{0}}!", permission);
	}

}