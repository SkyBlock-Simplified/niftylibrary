package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitTabCommand extends BukkitCommand implements TabExecutor {

	public BukkitTabCommand(JavaPlugin plugin) {
		this(plugin, null);
	}

	public BukkitTabCommand(JavaPlugin plugin, String command) {
		this(plugin, command, true);
	}

	public BukkitTabCommand(JavaPlugin plugin, boolean requireArgs) {
		this(plugin, null, requireArgs);
	}

	public BukkitTabCommand(JavaPlugin plugin, String command, boolean requireArgs) {
		this(plugin, command, requireArgs, true);
	}

	public BukkitTabCommand(JavaPlugin plugin, boolean requireArgs, boolean checkPerms) {
		this(plugin, null, requireArgs, checkPerms);
	}

	public BukkitTabCommand(JavaPlugin plugin, String command, boolean requireArgs, boolean checkPerms) {
		super(plugin, command, requireArgs, checkPerms);
		this.getCommand().setTabCompleter(this);
	}

	public abstract List<String> tabComplete(CommandSender sender, String[] args);

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (StringUtil.isEmpty(this.getCommand().getPermission()) || this.hasPermissions(sender, this.getCommand().getPermission()))
			return this.tabComplete(sender, args);
		else
			return Collections.emptyList();
	}

	protected static <T> List<T> iterableToList(Iterator<T> list) {
		List<T> copy = new ArrayList<>();
		while (list.hasNext()) copy.add(list.next());
		return copy;
	}

}