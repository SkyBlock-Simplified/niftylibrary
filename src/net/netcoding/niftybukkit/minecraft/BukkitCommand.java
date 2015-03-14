package net.netcoding.niftybukkit.minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A command wrapper that assists with common tasks like checking execution permissions,
 * only allowing console or player execution, has default permissions and overrides
 * bukkits default permission errors. Also has a simple method to show custom usage
 * messages to {@link CommandSender}.
 */
public abstract class BukkitCommand extends BukkitHelper implements CommandExecutor {

	private PluginCommand command = null;
	private boolean consoleOnly = false;
	private boolean playerOnly = false;
	private boolean checkPerms = true;
	private boolean bungeeOnly = false;
	private boolean helpCheck = true;
	private int minimumArgsLength = 1;
	private int maximumArgsLength = -1;
	private Map<Integer, Map<String, String>> usages = new HashMap<>();
	private Map<String, String[]> argCache = new HashMap<>();
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
		this.getCommand().setExecutor(this);
	}

	public abstract void onCommand(CommandSender sender, String alias, String[] args) throws Exception;

	/**
	 * Sets a custom usage based on label or argument.
	 * 
	 * @param index index of the argument. 0 will modify the command usage based on label, 1 or more will modify based on argument index
	 * @param arg the label/argument to check
	 * @param usage the usage to display (after the label/argument)
	 */
	public void editUsage(int index, String arg, String usage) {
		if (!this.usages.containsKey(index))
			this.usages.put(index, new HashMap<String, String>());

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
	 * Checks if the arguments are looking for help.
	 * 
	 * @param args Arguments to check if are looking for help.
	 * @return True if looking for help, otherwise false.
	 */
	protected boolean isHelp(String... args) {
		if (args.length > 0 && args[args.length - 1].matches("^[\\?]{1,}|help$"))
			return true;
		else
			return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		this.processCommand(sender, label, args);
		this.removeArgs(sender, args);
		return true;
	}

	private void processCommand(CommandSender sender, String label, String[] args) {
		if (this.isConsoleOnly() && isPlayer(sender)) {
			this.getLog().error(sender, "The command {{0}} is only possible from console!", this.getCommand().getName());
			return;
		}

		if (this.isPlayerOnly() && isConsole(sender)) {
			this.getLog().error(sender, "The command {0} is not possible from console!", this.getCommand().getName());
			return;
		}

		if (this.isBungeeOnly() && !NiftyBukkit.getBungeeHelper().isOnline()) {
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

		try {
			this.onCommand(sender, label, args);
		} catch (Exception ex) {
			this.getLog().console(ex);
		}
	}

	private void removeArgs(CommandSender sender, String... args) {
		if (this.argCache.containsKey(sender.getName()))
			this.argCache.remove(sender.getName());
	}

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
		String[] args = this.argCache.get(sender.getName());
		List<String> argList = this.getProperArgs(args);
		int index = argList.size();

		if (this.usages.containsKey(index)) {
			Map<String, String> usageMap = this.usages.get(index);
			String lastArg = index == 0 ? label : this.getLastArg(args);

			if (usageMap.containsKey(lastArg)) {
				String argStart = (index > 0 ? StringUtil.format("{0} ", StringUtil.implode(" ", argList)) : "");
				usage = StringUtil.format("/{0} {1}{2}", label, argStart, usageMap.get(lastArg));
			}
		}

		this.getLog().message(sender, this.getLog().getPrefix("Usage") + " " + usage);
	}

}