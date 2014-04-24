package net.netcoding.niftybukkit.minecraft;

import java.sql.SQLException;
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

	private transient PluginCommand command = null;
	private transient boolean requireArgs = true;
	private transient boolean consoleOnly = false;
	private transient boolean playerOnly = false;
	private transient boolean checkPerms = true;
	private transient boolean defaultPermsError = true;
	private transient Map<Integer, Map<String, String>> usages = new HashMap<>();
	private transient Map<String, String[]> argCache = new HashMap<>();
	private transient String permission;

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

	public abstract void onCommand(CommandSender sender, String alias, String[] args) throws SQLException, Exception;

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

	public boolean isArgsRequired() {
		return this.requireArgs;
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
		else if (this.requireArgs && args.length == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (this.isConsoleOnly() && isPlayer(sender)) {
			this.getLog().error(sender, "The command {{0}} is only possible from console!", this.getCommand().getName());
			return true;
		}

		if (this.isPlayerOnly() && isConsole(sender)) {
			this.getLog().error(sender, "The command {0} is not possible from console!", this.getCommand().getName());
			return true;
		}

		if (isPlayer(sender)) {
			if (this.isCheckingPerms() && !sender.hasPermission(this.permission)) {
				if (this.isUsingDefaultPermsError())
					this.noPerms(sender, this.permission);

				return true;
			}
		}

		if (!this.storeArgs(sender, args)) {
			this.getLog().error(sender, "Processing previous request. Try again in a second.");
			return true;
		}

		if (this.isHelp(args))
			this.showUsage(sender);
		else {
			try {
				this.onCommand(sender, label, args);
			} catch (Exception ex) {
				this.getLog().console(ex);
			}
		}

		this.removeArgs(sender, args);
		return true;
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

	public void setPlayerOnly() {
		this.setPlayerOnly(true);
	}

	public void setPlayerOnly(boolean value) {
		this.consoleOnly = false;
		this.playerOnly = value;
	}

	public void setRequireArgs() {
		this.setRequireArgs(true);
	}

	public void setRequireArgs(boolean value) {
		this.requireArgs = value;
	}

	private boolean storeArgs(CommandSender sender, String... args) {
		String senderName = sender.getName();

		if (!this.argCache.containsKey(senderName))
			this.argCache.put(senderName, args);
		else
			return false;

		return true;
	}

	public void showUsage(CommandSender sender) {
		String usage = this.getCommand().getUsage().replace("<command>", this.getCommand().getName());
		String[] args = this.argCache.get(sender.getName());
		List<String> argList = this.getProperArgs(args);
		int index = argList.size();

		if (this.usages.containsKey(index)) {
			Map<String, String> usageMap = this.usages.get(index);
			String lastArg  = this.getLastArg(args);

			if (usageMap.containsKey(lastArg)) {
				String argStart = (argList.size() > 0 ? String.format("%1$s ", StringUtil.implode(" ", argList)) : "");
				usage = String.format("/%1$s %2$s%3$s", this.getCommand().getName(), argStart, usageMap.get(lastArg));
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