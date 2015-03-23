package net.netcoding.niftybukkit.minecraft;

import java.util.concurrent.TimeUnit;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.concurrent.ConcurrentList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

	public final static ConcurrentList<String> getPluginCache() {
		return PLUGINS;
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
		String permission = StringUtil.format("{0}.{1}", this.getDescription().getName().toLowerCase(), StringUtil.implode(".", permissions));
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