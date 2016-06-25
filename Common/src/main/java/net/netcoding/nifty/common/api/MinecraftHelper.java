package net.netcoding.nifty.common.api;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.IMinecraftPlugin;
import net.netcoding.nifty.common.minecraft.command.source.CommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.core.api.plugin.PluginHelper;
import net.netcoding.nifty.core.util.StringUtil;

public abstract class MinecraftHelper implements PluginHelper<MinecraftLogger> {

	private final transient IMinecraftPlugin plugin;

	protected MinecraftHelper(IMinecraftPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public final MinecraftLogger getLog() {
		return this.getPlugin().getLog();
	}

	public final IMinecraftPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public final PluginDescription getPluginDescription() {
		return this.getPlugin().getPluginDescription();
	}

	public final boolean hasPermissions(BukkitMojangProfile profile, String... permissions) {
		return this.hasPermissions(profile, false, permissions);
	}

	public final boolean hasPermissions(BukkitMojangProfile profile, boolean defaultError, String... permissions) {
		return profile.getOfflinePlayer().isOnline() && this.hasPermissions(profile.getOfflinePlayer().getPlayer(), defaultError, permissions);
	}

	public final boolean hasPermissions(CommandSource sender, String... permissions) {
		return this.hasPermissions(sender, false, permissions);
	}

	public final boolean hasPermissions(CommandSource sender, boolean defaultError, String... permissions) {
		if (isConsole(sender)) return true;
		String permission = StringUtil.format("{0}.{1}", this.getPluginDescription().getName().toLowerCase(), StringUtil.implode(".", permissions));
		boolean hasPerms = sender.hasPermission(permission);
		if (!hasPerms && defaultError) this.noPerms(sender, permission);
		return hasPerms;
	}

	public static boolean isConsole(CommandSource sender) {
		return isConsole(sender.getName());
	}

	public static boolean isConsole(String senderName) {
		return Nifty.getServer().getConsoleSource().getName().equals(senderName) || "@".equals(senderName);
	}

	public static boolean isPlayer(CommandSource sender) {
		return isPlayer(sender.getName());
	}

	public static boolean isPlayer(String senderName) {
		return !isConsole(senderName);
	}

	public static Player findPlayer(String playerName) {
		playerName = playerName.toLowerCase();

		for (Player player : Nifty.getServer().getPlayerList()) {
			if (player.getName().equalsIgnoreCase(playerName))
				return player;
		}

		for (Player player : Nifty.getServer().getPlayerList()) {
			if (player.getName().toLowerCase().startsWith(playerName))
				return player;
		}

		return null;
	}

	void noPerms(CommandSource sender, String permission) {
		this.getLog().error(sender, "You do not have the permission {{0}}!", permission);
	}

}