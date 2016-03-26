package net.netcoding.niftybukkit.minecraft;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.messages.BungeeServer;
import net.netcoding.niftycore.minecraft.scheduler.MinecraftScheduler;
import net.netcoding.niftycore.mojang.MojangProfile;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A command wrapper that assists with common tasks like checking execution permissions,
 * only allowing console or player execution, has default permissions and overrides
 * bukkits default permission errors. Also has a simple method to show custom usage
 * messages to {@link CommandSender}.
 */
public abstract class BukkitCommand extends BukkitHelper {

	private final static transient ConcurrentMap<String, Integer> PLUGINS = new ConcurrentMap<>();
	private final ConcurrentMap<Integer, ConcurrentMap<String, String>> usages = new ConcurrentMap<>();
	private final ConcurrentMap<String, String[]> argCache = new ConcurrentMap<>();
	private PluginCommand command = null;
	private boolean consoleOnly = false;
	private boolean playerOnly = false;
	private boolean checkPerms = true;
	private boolean bungeeOnly = false;
	private boolean helpCheck = true;
	private boolean playerTabComplete = false;
	private int playerTabCompleteIndex = 0;
	private int minimumArgsLength = 1;
	private int maximumArgsLength = -1;
	private String permission;

	/**
	 * Creates a command wrapper.
	 *
	 * @param plugin the plugin to load the command from
	 * @param command the command name for this class
	 */
	public BukkitCommand(JavaPlugin plugin, String command) {
		super(plugin);
		if (StringUtil.isEmpty(command)) throw new IllegalArgumentException("Command cannot be null!");
		if (this.getPlugin().getCommand(command) == null) throw new RuntimeException(StringUtil.format("Command ''{0}'' not defined in plugin {1}!", command, this.getPluginDescription().getName()));
		this.command = this.getPlugin().getCommand(command);
		this.permission = StringUtil.notEmpty(this.getCommand().getPermission()) ? this.getCommand().getPermission() : StringUtil.format("{0}.{1}", this.getPluginDescription().getName().toLowerCase(), command);
		this.getCommand().setPermission("");
		this.getCommand().setPermissionMessage("");
		this.getCommand().setExecutor(new BukkitCommandExecutor(this));
		this.getCommand().setTabCompleter(new BukkitTabCompleter(this));

		if (!PLUGINS.keySet().contains(this.getPlugin().getName()))
			PLUGINS.put(this.getPlugin().getName(), 1);
		else
			PLUGINS.put(this.getPlugin().getName(), PLUGINS.get(this.getPlugin().getName()) + 1);
	}

	/**
	 * Sets a custom usage based on label or argument.
	 *
	 * @param index index of the argument. 0 will modify the command usage based on label, 1 or more will modify based on argument index
	 * @param arg the label/argument to check
	 * @param usage the usage to display (after the label/argument)
	 */
	public void editUsage(int index, String arg, String usage) {
		if (!this.usages.containsKey(index))
			this.usages.put(index, new ConcurrentMap<String, String>());

		this.usages.get(index).put(arg, usage);
	}

	/**
	 * Gets the command created for your plugin.
	 *
	 * @return Command associated to this class
	 */
	public PluginCommand getCommand() {
		return this.command;
	}

	private String getLastArg(String... args) {
		List<String> properArgs = this.getProperArgs(args);
		int size = properArgs.size();
		return (size > 0 ? properArgs.get(size - 1) : "");
	}

	public static int getPluginCache(String pluginName) {
		return PLUGINS.keySet().contains(pluginName) ? PLUGINS.get(pluginName) : 0;
	}

	private List<String> getProperArgs(String... args) {
		List<String> argList = StringUtil.toList(args);
		int size = argList.size();

		if (size > 0) {
			size--;

			if (argList.get(size).matches("^\\?|help$"))
				argList.remove(size);
		}

		return argList;
	}

	/**
	 * Checks if this command can only be run when BungeeCord is detected.
	 *
	 * @return True if bungee detected, otherwise false.
	 */
	public boolean isBungeeOnly() {
		return this.bungeeOnly;
	}

	/**
	 * Checks if the arguments are being looked at for help.
	 *
	 * @return True if checking, otherwise false.
	 */
	public boolean isCheckingHelp() {
		return this.helpCheck;
	}

	/**
	 * Checks if the users permissions will be checked immediately upon running the command.
	 *
	 * @return True if checking, otherwise false.
	 */
	public boolean isCheckingPerms() {
		return this.checkPerms;
	}

	/**
	 * Checks if this command can only be ran as console.
	 *
	 * @return True if console only, otherwise false.
	 */
	public boolean isConsoleOnly() {
		return this.consoleOnly;
	}

	/**
	 * Checks if this command can only be ran as a player.
	 *
	 * @return True if player only, otherwise false.
	 */
	public boolean isPlayerOnly() {
		return this.playerOnly;
	}

	/**
	 * Checks if this command will tab complete for player names.
	 *
	 * @return True if auto tab completing, otherwise false.
	 */
	public boolean isPlayerTabComplete() {
		return this.playerTabComplete;
	}

	/**
	 * Checks if the arguments are looking for help.
	 *
	 * @param args Arguments to check if are looking for help.
	 * @return True if looking for help, otherwise false.
	 */
	protected boolean isHelp(String... args) {
		return (args.length > 0 && args[args.length - 1].matches("^[\\?]+|help$"));
	}

	protected abstract void onCommand(CommandSender sender, String alias, String[] args) throws Exception;

	protected List<String> onTabComplete(CommandSender sender, String label, String[] args) throws Exception {
		return Collections.emptyList();
	}

	private void processCommand(final CommandSender sender, final String label, final String[] args) {
		if (this.isConsoleOnly() && isPlayer(sender)) {
			this.getLog().error(sender, "The command {{0}} is only possible from console!", this.getCommand().getName());
			return;
		}

		if (this.isPlayerOnly() && isConsole(sender)) {
			this.getLog().error(sender, "The command {0} is not possible from console!", this.getCommand().getName());
			return;
		}

		if (this.isBungeeOnly() && !NiftyBukkit.getBungeeHelper().isDetected()) {
			this.getLog().error(sender, "The command {{0}} requires BungeeCord!", this.getCommand().getName());
			return;
		}

		if (isPlayer(sender)) {
			if (this.isCheckingPerms() && !sender.hasPermission(this.permission)) {
				this.noPerms(sender, this.permission);
				return;
			}
		}

		if (this.minimumArgsLength > 0 && args.length < this.minimumArgsLength) {
			this.showUsage(sender, label);
			return;
		}

		if (this.maximumArgsLength > 0 && args.length > this.maximumArgsLength) {
			this.showUsage(sender, label);
			return;
		}

		if (!this.storeArgs(sender, args)) {
			this.getLog().error(sender, "Processing previous request. Try again in a second.");
			return;
		}

		if (this.isCheckingHelp() && this.isHelp(args)) {
			this.showUsage(sender, label);
			return;
		}

		MinecraftScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					onCommand(sender, label, args);
				} catch (Exception ex) {
					getLog().console(ex);
				}

				if (argCache.containsKey(sender.getName()))
					argCache.remove(sender.getName());
			}
		});
	}

	public static List<String> getMatchingPlayers(String lookup) {
		List<String> names = new ArrayList<>();

		if (NiftyBukkit.getBungeeHelper().isDetected()) {
			for (BungeeServer server : NiftyBukkit.getBungeeHelper().getServers()) {
				for (MojangProfile profile : server.getPlayerList()) {
					if (profile.getName().toLowerCase().startsWith(lookup) || profile.getName().toLowerCase().contains(lookup))
						names.add(profile.getName());
				}
			}
		} else {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getName().toLowerCase().startsWith(lookup) || player.getName().toLowerCase().contains(lookup))
					names.add(player.getName());
			}
		}

		return names;
	}

	private List<String> processTabComplete(CommandSender sender, String label, String[] args) {
		List<String> complete = new ArrayList<>();

		if (isConsole(sender))
			return Collections.emptyList();

		if (this.isBungeeOnly() && !NiftyBukkit.getBungeeHelper().isDetected())
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
	}

	/*private void removeArgs(CommandSender sender) {
		if (this.argCache.containsKey(sender.getName()))
		this.argCache.remove(sender.getName());
	}*/

	/**
	 * Sets command to run only if BungeeCord is detected.
	 */
	public void setBungeeOnly() {
		this.setBungeeOnly(true);
	}

	/**
	 * Sets command to run only if BungeeCord is detected.
	 *
	 * @param value True to run only when detected, otherwise false.
	 */
	public void setBungeeOnly(boolean value) {
		this.bungeeOnly = value;
	}

	/**
	 * Sets command to check if user has permission to run.
	 */
	public void setCheckPerms() {
		this.setCheckPerms(true);
	}

	/**
	 * Sets command to check if user has permission to run.
	 *
	 * @param value true to check permissions (default), otherwise false
	 */
	public void setCheckPerms(boolean value) {
		this.checkPerms = value;
	}

	/**
	 * Sets command to only allow console execution. This command sets {@link #setPlayerOnly() setPlayerOnly} to false.
	 */
	public void setConsoleOnly() {
		this.setConsoleOnly(true);
	}

	/**
	 * Sets command to only allow console execution. This command sets {@link #setPlayerOnly() setPlayerOnly} to false.
	 *
	 * @param value true to only allow console, otherwise false
	 */
	public void setConsoleOnly(boolean value) {
		this.playerOnly = false;
		this.consoleOnly = value;
	}

	/**
	 * Sets command to check for help.
	 */
	public void setCheckHelp() {
		this.setCheckHelp(true);
	}

	/**
	 * Sets command to check for help.
	 *
	 * @param value true to only allow console, otherwise false
	 */
	public void setCheckHelp(boolean value) {
		this.helpCheck = value;
	}

	/**
	 * Sets the maximum arguments that can be passed to this command.
	 *
	 * @param value Maximum number of arguments allowed.
	 */
	public void setMaximumArgsLength(int value) {
		this.maximumArgsLength = value;
	}

	/**
	 * Sets the minimum arguments that need to be passed to this command.
	 *
	 * @param value Minimum number of arguments allowed.
	 */
	public void setMinimumArgsLength(int value) {
		this.minimumArgsLength = value;
	}

	/**
	 * Sets command to only allow player execution. This command sets {@link #setConsoleOnly() setConsoleOnly} to false.
	 */
	public void setPlayerOnly() {
		this.setPlayerOnly(true);
	}

	/**
	 * Sets command to only allow player execution. This command sets {@link #setConsoleOnly() setConsoleOnly} to false.
	 *
	 * @param value True to only allow players, otherwise false
	 */
	public void setPlayerOnly(boolean value) {
		this.consoleOnly = false;
		this.playerOnly = value;
	}

	/**
	 * Sets this command to automatically tab complete player names.
	 */
	public void setPlayerTabComplete() {
		this.setPlayerTabComplete(true);
	}

	/**
	 * Sets this command to automatically tab complete player names.
	 *
	 * @param value True to enable player tab complete, otherwise false.
	 */
	public void setPlayerTabComplete(boolean value) {
		this.playerTabComplete = value;
	}

	/**
	 * Sets this index to use when automatically tab completing player names.
	 *
	 * @param value Index to tab complete player names.
	 */
	public void setPlayerTabCompleteIndex(int value) {
		this.playerTabCompleteIndex = value >= 0 ? value : 0;
	}

	private boolean storeArgs(CommandSender sender, String... args) {
		if (!this.argCache.containsKey(sender.getName())) {
			this.argCache.put(sender.getName(), args);
			return true;
		}

		return false;
	}

	/**
	 * Shows command usage if arguments end in a question mark or help.
	 * Also if they have too few or too many arguments.
	 * <p>
	 * Defaults label to {@link PluginCommand#getName()}
	 *
	 * @param sender The sender to send the usage to
	 */
	public void showUsage(CommandSender sender) {
		this.showUsage(sender, this.getCommand().getName());
	}

	/**
	 * Shows command usage if arguments end in a question mark or help.
	 * Also if they have too few or too many arguments.
	 *
	 * @param sender The sender to send the usage to
	 * @param label The label to use in the usage (defaults to {@link PluginCommand#getName()})
	 */
	public void showUsage(CommandSender sender, String label) {
		String usage = this.getCommand().getUsage().replace("<command>", StringUtil.notEmpty(label) ? label : this.getCommand().getName());
		String[] args = ListUtil.toArray(this.getProperArgs(this.argCache.get(sender.getName())), String.class);

		for (int i = args.length; i > 0; i--) {
			if (this.usages.containsKey(i)) {
				ConcurrentMap<String, String> usageMap = this.usages.get(i);
				String lastArg = (i == 0 ? label : this.getLastArg(args));

				if (usageMap.containsKey(lastArg)) {
					String argStart = (i > 0 ? StringUtil.format("{0} ", StringUtil.implode(" ", args)) : "");
					usage = StringUtil.format("/{0} {1}{2}", label, argStart, usageMap.get(lastArg));
				}

				break;
			} else
				args = StringUtil.split(" ", StringUtil.implode(" ", args, 0, args.length - 1));
		}

		this.getLog().message(sender, this.getLog().getPrefix("Usage") + " " + usage);
	}

	private class BukkitCommandExecutor implements CommandExecutor {

		private final BukkitCommand command;

		public BukkitCommandExecutor(BukkitCommand command) {
			this.command = command;
		}

		public BukkitCommand getCommand() {
			return this.command;
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			this.getCommand().processCommand(sender, label, args);
			//this.getCommand().removeArgs(sender);
			return true;
		}

	}

	private class BukkitTabCompleter implements TabCompleter {

		private final BukkitCommand command;

		public BukkitTabCompleter(BukkitCommand command) {
			this.command = command;
		}

		public BukkitCommand getCommand() {
			return this.command;
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
			return this.getCommand().processTabComplete(sender, label, args);
		}

	}

}