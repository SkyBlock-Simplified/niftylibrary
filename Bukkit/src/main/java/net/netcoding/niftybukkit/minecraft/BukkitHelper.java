package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitHelper {

	private final static transient ConcurrentList<String> PLUGINS = new ConcurrentList<>();
	private final transient JavaPlugin javaPlugin;
	private final transient BukkitLogger logger;

	public BukkitHelper(JavaPlugin plugin) {
		this.javaPlugin = plugin;
		this.logger = new BukkitLogger(plugin);
		PLUGINS.add(this.getPluginDescription().getName());
	}

	public final BukkitLogger getLog() {
		return this.logger;
	}

	public final JavaPlugin getPlugin() {
		return this.javaPlugin;
	}

	public final <T extends JavaPlugin> T getPlugin(Class<T> plugin) {
		return plugin.cast(this.getPlugin());
	}

	public static ConcurrentList<String> getPluginCache() {
		return PLUGINS;
	}

	public final PluginDescriptionFile getPluginDescription() {
		return this.getPlugin().getDescription();
	}

	public final boolean hasPermissions(BukkitMojangProfile profile, String... permissions) {
		return this.hasPermissions(profile, false, permissions);
	}

	public final boolean hasPermissions(BukkitMojangProfile profile, boolean defaultError, String... permissions) {
		return profile.getOfflinePlayer().isOnline() && this.hasPermissions(profile.getOfflinePlayer().getPlayer(), defaultError, permissions);
	}

	public final boolean hasPermissions(CommandSender sender, String... permissions) {
		return this.hasPermissions(sender, false, permissions);
	}

	public final boolean hasPermissions(CommandSender sender, boolean defaultError, String... permissions) {
		if (isConsole(sender)) return true;
		String permission = StringUtil.format("{0}.{1}", this.getPlugin().getDescription().getName().toLowerCase(), StringUtil.implode(".", permissions));
		boolean hasPerms = sender.hasPermission(permission);
		if (!hasPerms && defaultError) this.noPerms(sender, permission);
		return hasPerms;
	}

	public static boolean isConsole(CommandSender sender) {
		return isConsole(sender.getName());
	}

	public static boolean isConsole(String senderName) {
		return Bukkit.getConsoleSender().getName().equals(senderName) || "@".equals(senderName);
	}

	public static boolean isPlayer(CommandSender sender) {
		return isPlayer(sender.getName());
	}

	public static boolean isPlayer(String senderName) {
		return !isConsole(senderName);
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