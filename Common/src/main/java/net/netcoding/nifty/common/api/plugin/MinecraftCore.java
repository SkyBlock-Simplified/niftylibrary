package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.BlockCommandSource;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.command.ConsoleCommandSource;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.core.api.plugin.PluginHelper;
import net.netcoding.nifty.core.util.StringUtil;

abstract class MinecraftCore implements PluginHelper<MinecraftLogger> {

	public static <T extends MinecraftPlugin> T getPlugin(Class<T> plugin) {
		return Nifty.getPluginManager().getPlugin(plugin);
	}

	public final boolean hasPermissions(MinecraftMojangProfile profile, String... permissions) {
		return this.hasPermissions(profile, false, permissions);
	}

	public final boolean hasPermissions(MinecraftMojangProfile profile, boolean defaultError, String... permissions) {
		return profile.getOfflinePlayer().isOnline() && this.hasPermissions(profile.getOfflinePlayer().getPlayer(), defaultError, permissions);
	}

	public final boolean hasPermissions(CommandSource source, String... permissions) {
		return this.hasPermissions(source, false, permissions);
	}

	public final boolean hasPermissions(CommandSource source, boolean defaultError, String... permissions) {
		if (isConsole(source)) return true;
		String permission = StringUtil.format("{0}.{1}", this.getDesc().getName().toLowerCase(), StringUtil.implode(".", permissions));
		boolean hasPerms = source.hasPermission(permission);
		if (!hasPerms && defaultError) noPerms(this.getLog(), source, permission);
		return hasPerms;
	}

	public static boolean isConsole(CommandSource source) {
		return (source instanceof ConsoleCommandSource) || (source instanceof BlockCommandSource);
	}

	public static boolean isConsole(String senderName) {
		return !isPlayer(senderName) && Nifty.getServer().getConsoleSource().getName().equals(senderName) || "@".equals(senderName);
	}

	public static boolean isPlayer(CommandSource source) {
		return !isConsole(source);
	}

	public static boolean isPlayer(String senderName) {
		return Nifty.getServer().getPlayer(senderName, true) != null;
	}

	static void noPerms(MinecraftLogger log, CommandSource sender, String permission) {
		log.error(sender, "You do not have the permission {{0}}!", permission);
	}

}