package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public abstract class BukkitPlugin extends JavaPlugin {

	private final static transient ConcurrentList<String> PLUGINS = new ConcurrentList<>();
	private final transient BukkitLogger log;
	private long enable = System.currentTimeMillis();

	public BukkitPlugin() {
		this.log = new BukkitLogger(this);
		PLUGINS.add(this.getDescription().getName());
	}

	public BukkitLogger getLog() {
		return this.log;
	}

	public static ConcurrentList<String> getPluginCache() {
		return PLUGINS;
	}

	public boolean hasPermissions(BukkitMojangProfile profile, String... permissions) {
		return this.hasPermissions(profile, false, permissions);
	}

	public boolean hasPermissions(BukkitMojangProfile profile, boolean defaultError, String... permissions) {
		return profile.getOfflinePlayer().isOnline() && this.hasPermissions(profile.getOfflinePlayer().getPlayer(), defaultError, permissions);
	}

	public boolean hasPermissions(CommandSender sender, String... permissions) {
		return this.hasPermissions(sender, false, permissions);
	}

	public boolean hasPermissions(CommandSender sender, boolean defaultError, String... permissions) {
		if (isConsole(sender)) return true;
		String permission = StringUtil.format("{0}.{1}", this.getDescription().getName().toLowerCase(), StringUtil.implode(".", permissions));
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

	public final void startLoggingTime() {
		this.enable = System.currentTimeMillis();
	}

	public final void showRunningTime() {
		this.showRunningTime(TimeUnit.MILLISECONDS);
	}

	public final void showRunningTime(TimeUnit time) {
		this.getLog().console("Running time: {0} {1}", time.convert((System.currentTimeMillis() - this.enable), TimeUnit.MILLISECONDS), time.name().toLowerCase());
	}

}