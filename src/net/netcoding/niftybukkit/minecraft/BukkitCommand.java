package net.netcoding.niftybukkit.minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitCommand extends BukkitHelper implements CommandExecutor {

	private PluginCommand command = null;
	private boolean consoleOnly = false;
	private boolean playerOnly = false;
	private boolean checkPerms = true;
	private boolean defaultPermsError = true;
	private int minimumArgsLength = 1;
	private int maximumArgsLength = -1;
	private Map<Integer, Map<String, String>> usages = new HashMap<>();
	private Map<String, String[]> argCache = new HashMap<>();
	private String permission;

	public BukkitCommand(JavaPlugin plugin, String command) {
		super(plugin);
		if (StringUtil.isEmpty(command)) throw new IllegalArgumentException("Command cannot be null!");
		this.command = this.getPlugin().getCommand(command);
		this.permission = String.format("%s.%s", this.getPluginDescription().getName().toLowerCase(), command);
		this.getCommand().setPermissionMessage("");

		if (StringUtil.notEmpty(this.getCommand().getPermission())) {
			this.permission = this.getCommand().getPermission();
			this.getCommand().setPermission("");
		}

		this.getCommand().setExecutor(this);
	}

	public abstract void onCommand(CommandSender sender, String alias, String[] args) throws Exception;

	public void editUsage(int index, String arg, String usage) {
		if (this.usages.containsKey(index)) {
			if (index == 0) arg = "";
			this.usages.get(index).put(arg, usage);
		} else {
			Map<String, String> usageMap = new HashMap<>();
			usageMap.put(arg, usage);
			this.usages.put(index, usageMap);
		}
	}

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

	public boolean isCheckingPerms() {
		return this.checkPerms;
	}

	public boolean isConsoleOnly() {
		return this.consoleOnly;
	}

	public boolean isPlayerOnly() {
		return this.playerOnly;
	}

	public boolean isUsingDefaultPermsError() {
		return this.defaultPermsError;
	}

	private boolean isHelp(String... args) {
		if (args.length > 0 && args[args.length - 1].matches("^[\\?]{1,}|help$"))
			return true;
		else
			return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		this.processCommand(sender, command, label, args);
		this.removeArgs(sender, args);
		return true;
	}

	private void processCommand(CommandSender sender, Command command, String label, String[] args) {
		if (this.isConsoleOnly() && isPlayer(sender)) {
			this.getLog().error(sender, "The command {{0}} is only possible from console!", this.getCommand().getName());
			return;
		}

		if (this.isPlayerOnly() && isConsole(sender)) {
			this.getLog().error(sender, "The command {0} is not possible from console!", this.getCommand().getName());
			return;
		}

		if (isPlayer(sender)) {
			if (this.isCheckingPerms() && !sender.hasPermission(this.permission)) {
				if (this.isUsingDefaultPermsError())
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

		if (this.isHelp(args)) {
			this.showUsage(sender, label);
			return;
		}

		if (!this.storeArgs(sender, args)) {
			this.getLog().error(sender, "Processing previous request. Try again in a second.");
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

	public void setCheckPerms() {
		this.setCheckPerms(true);
	}

	public void setCheckPerms(boolean value) {
		this.checkPerms = value;
	}

	public void setConsoleOnly() {
		this.setConsoleOnly(true);
	}

	public void setConsoleOnly(boolean value) {
		this.playerOnly = false;
		this.consoleOnly = value;
	}

	public void setMaximumArgsLength(int value) {
		this.maximumArgsLength = value;
	}

	public void setMinimumArgsLength(int value) {
		this.minimumArgsLength = value;
	}

	public void setPlayerOnly() {
		this.setPlayerOnly(true);
	}

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

	public void showUsage(CommandSender sender) {
		this.showUsage(sender, this.getCommand().getName());
	}

	public void showUsage(CommandSender sender, String label) {
		String usage = this.getCommand().getUsage().replace("<command>", label);
		String[] args = this.argCache.get(sender.getName());
		List<String> argList = this.getProperArgs(args);
		int index = argList.size();

		if (this.usages.containsKey(index)) {
			Map<String, String> usageMap = this.usages.get(index);
			String lastArg  = this.getLastArg(args);

			if (usageMap.containsKey(lastArg)) {
				String argStart = (argList.size() > 0 ? String.format("%1$s ", StringUtil.implode(" ", argList)) : "");
				usage = String.format("/%1$s %2$s%3$s", label, argStart, usageMap.get(lastArg));
			}
		}

		this.getLog().message(sender, this.getLog().getPrefix("Usage") + " " + usage);
	}

	public void useDefaultPermsError() {
		this.useDefaultPermsError(true);
	}

	public void useDefaultPermsError(boolean value) {
		this.defaultPermsError = value;
	}

}