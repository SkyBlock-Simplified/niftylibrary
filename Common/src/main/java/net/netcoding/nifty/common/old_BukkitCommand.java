package net.netcoding.nifty.common;

import net.netcoding.nifty.common.api.plugin.MinecraftHelper;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;

public abstract class old_BukkitCommand extends MinecraftHelper {

	public old_BukkitCommand(MinecraftPlugin plugin, String command) {
		super(plugin);
		/*if (StringUtil.isEmpty(command)) throw new IllegalArgumentException("Command cannot be null!");
		if (this.getPlugin().getCommand(command) == null) throw new RuntimeException(StringUtil.format("Command ''{0}'' not defined in plugin {1}!", command, this.getPluginDescription().getName()));
		this.command = this.getPlugin().getCommand(command);
		this.permission = StringUtil.notEmpty(this.getCommand().getPermission()) ? this.getCommand().getPermission() : StringUtil.format("{0}.{1}", this.getPluginDescription().getName().toLowerCase(), command);
		this.getCommand().setPermission("");
		this.getCommand().setPermissionMessage("");
		this.getCommand().setExecutor(new BukkitCommandExecutor(this));
		this.getCommand().setTabCompleter(new BukkitTabCompleter(this));*/
	}

	/*public static List<String> getMatchingPlayers(String lookup) {
		List<String> names = new ArrayList<>();

		if (Nifty.getBungeeHelper().getDetails().isDetected()) {
			for (BungeeServer<BukkitMojangProfile> server : Nifty.getBungeeHelper().getServers()) {
				for (BukkitMojangProfile profile : server.getPlayerList()) {
					if (profile.getName().toLowerCase().startsWith(lookup) || profile.getName().toLowerCase().contains(lookup))
						names.add(profile.getName());
				}
			}
		} else {
			for (Player player : Nifty.getServer().getPlayerList()) {
				if (player.getName().toLowerCase().startsWith(lookup) || player.getName().toLowerCase().contains(lookup))
					names.add(player.getName());
			}
		}

		return names;
	}*/

	/*private List<String> processTabComplete(CommandSender sender, String label, String[] args) {
		List<String> complete = new ArrayList<>();

		if (isConsole(sender))
			return Collections.emptyList();

		if (this.isBungeeOnly() && !Nifty.getBungeeHelper().isDetected())
			return Collections.emptyList();

		if (this.isCheckingPerms() && !sender.hasPermission(this.permission))
			return Collections.emptyList();

		if (this.isPlayerTabComplete() && ListUtil.notEmpty(args) && this.playerTabCompleteIndex == args.length - 1)
			complete.addAll(getMatchingPlayers(args[this.playerTabCompleteIndex].toLowerCase()));

		try {
			complete.addAll(this.onTabComplete(sender, label, args));
		} catch (Exception ex) {
			this.getLog().console(ex);
		}

		return ListUtil.notEmpty(complete) ? complete : Collections.<String>emptyList();
	}*/

	/*private class BukkitCommandExecutor implements CommandExecutor {

		private final old_BukkitCommand command;

		public BukkitCommandExecutor(old_BukkitCommand command) {
			this.command = command;
		}

		public old_BukkitCommand getCommand() {
			return this.command;
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			this.getCommand().processCommand(sender, label, args);
			this.getCommand().removeArgs(sender);
			return true;
		}

	}*/

	/*private class BukkitTabCompleter implements TabCompleter {

		private final old_BukkitCommand command;

		public BukkitTabCompleter(old_BukkitCommand command) {
			this.command = command;
		}

		public old_BukkitCommand getCommand() {
			return this.command;
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
			return this.getCommand().processTabComplete(sender, label, args);
		}

	}*/

}