package net.netcoding.niftybukkit.minecraft;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.utilities.StringUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitCommand extends BukkitHelper implements CommandExecutor {

	private transient PluginCommand command = null;
	private transient boolean requireArgs = true;
	private transient Map<Integer, Map<String, String>> usages = new HashMap<>();
	private transient Map<String, String[]> argCache = new HashMap<>();

	public BukkitCommand(JavaPlugin plugin, String command) {
		this(plugin, command, true);
	}

	public BukkitCommand(JavaPlugin plugin, String command, boolean requireArgs) {
		super(plugin);
		(this.command = this.getPlugin().getCommand(command)).setExecutor(this);
		this.setRequireArgs(requireArgs);
	}

	public abstract void command(CommandSender sender, String args[]) throws SQLException, Exception;

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

	@Override
	public boolean hasPermissions(CommandSender sender, String... permissions) {
		boolean hasPerms = super.hasPermissions(sender, permissions);
		if (!hasPerms) this.getLog().noPerms(sender, permissions);
		return hasPerms;
	}

	private boolean isHelp(String... args) {
		if (args.length > 0 && args[args.length - 1].matches("^\\?|help$"))
			return true;
		else if (this.requireArgs && args.length == 0)
			return true;
		else
			return false;
	}

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.storeArgs(sender, args)) {
			this.getLog().error(sender, "Processing previous request. Try again in a few seconds.");
			return true;
		}

		if (this.isHelp(args))
			this.showUsage(sender);
		else {
			try {
				this.command(sender, args);
			} catch (Exception ex) {
				this.getLog().console(ex);
			}
		}

		this.removeArgs(sender, args);
		return true;
	}

	public void removeArgs(CommandSender sender, String... args) {
		String senderName = sender.getName();
		if (this.argCache.containsKey(senderName)) this.argCache.remove(senderName);
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

}